package net.yunyi.back.common;

import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 处理自定义的业务异常
	 *
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BizException.class)
	@ResponseBody
	public ApiResult bizExceptionHandler(HttpServletRequest req, BizException e) {
		logger.error("Business exception, caused by {} ", e.getErrorMsg());
		return ApiResult.error(e.getErrorCode(), e.getErrorMsg());
	}

	/**
	 * 处理其他异常
	 *
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ApiResult exceptionHandler(HttpServletRequest req, Exception e) {
		int errorCode;
		String errorMsg;
		if (e instanceof ConstraintViolationException) {
			errorCode = YunyiCommonEnum.INVALID_REQUEST.getResultCode();
			errorMsg = "invalid input param: " + e.getMessage();
		} else {
			errorCode = YunyiCommonEnum.INTERNAL_SERVER_ERROR.getResultCode();
			errorMsg = "internal server error : " + e.getMessage();
		}
		return ApiResult.error(errorCode, errorMsg);
	}
}