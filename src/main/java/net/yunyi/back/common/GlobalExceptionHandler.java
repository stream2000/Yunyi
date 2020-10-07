package net.yunyi.back.common;

import javax.servlet.http.HttpServletRequest;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
        logger.error("Unknown exception, caused by {}", e.toString());
        return ApiResult.error(YunyiCommonEnum.INTERNAL_SERVER_ERROR);
    }
}