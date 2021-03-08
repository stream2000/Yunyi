package net.yunyi.back.controller;

import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.UploadTransParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@Validated
@RequestMapping("/trans")
public class TranslationController {
	@PostMapping("/upload")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "上传翻译接口, 返回翻译id")
	public ApiResult<Integer> uploadTranslation(@RequestAttribute("user") User user, @RequestBody UploadTransParam param) {
		for (UploadTransParam.TransSegment segment : param.getSegmentList()) {
			if (segment == null || segment.getRefSegIds() == null || segment.getRefSegIds().isEmpty()) {
				throw new BizException(YunyiCommonEnum.TRANS_PARAM_ERROR);
			}
			Collections.sort(segment.getRefSegIds());
		}
		return ApiResult.ok();
	}
}
