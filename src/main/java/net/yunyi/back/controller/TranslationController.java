package net.yunyi.back.controller;

import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.UploadTransParam;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	IArticleTransService articleTransService;

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
		return ApiResult.ok(articleTransService.uploadTranslation(user.getId().intValue(), param));
	}

	@PostMapping("/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除翻译")
	public ApiResult<Boolean> deleteTranslation(@RequestAttribute("user") User user, @PathVariable int id) {
		return ApiResult.ok(articleTransService.deleteTranslation(user.getId().intValue(), id));
	}

	@PostMapping("/{id}/modify")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除翻译")
	public ApiResult<Integer> modifyTranslation(@RequestAttribute("user") User user, @PathVariable int id, @RequestBody UploadTransParam param) {
		return ApiResult.ok(articleTransService.modifyTranslation(user.getId().intValue(), param));
	}
}
