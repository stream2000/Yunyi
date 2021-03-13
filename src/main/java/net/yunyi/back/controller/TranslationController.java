package net.yunyi.back.controller;

import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.AddTranslationCommentParam;
import net.yunyi.back.persistence.param.AddTranslationSegCommentParam;
import net.yunyi.back.persistence.param.UploadTransParam;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import net.yunyi.back.persistence.vo.ArticleCommentVo;
import net.yunyi.back.persistence.vo.SimpleTranslationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

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
	@ApiOperation(value = "修改翻译")
	public ApiResult<Integer> modifyTranslation(@RequestAttribute("user") User user, @PathVariable int id, @RequestBody UploadTransParam param) {
		return ApiResult.ok(articleTransService.modifyTranslation(user.getId().intValue(), param));
	}

	@GetMapping("/{id}")
	@ResponseBody
	@ApiOperation("获取一个翻译的具体内容")
	public ApiResult<SimpleTranslationVo> getTranslation(@PathVariable final int id) {
		return ApiResult.ok(articleTransService.getTranslation(id));
	}

	@PostMapping("/comment/add")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "添加对整个翻译的评论")
	public ApiResult<Integer> addTranslationComment(@RequestAttribute(value = "user") User user, @RequestBody AddTranslationCommentParam param) {
		return ApiResult.ok();
	}

	@PostMapping("/comment/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除对整个翻译的评论")
	public ApiResult<Boolean> deleteTranslationComment(@PathVariable int id) {
		return ApiResult.ok();
	}

	@GetMapping("/{id}/comments")
	@ResponseBody
	@ApiOperation(value = "获取翻译界面的评论")
	public ApiResult<List<ArticleCommentVo>> getTranslationComment(@RequestParam @Min(1) int pageId, @RequestParam @Min(1) int pageSize, @PathVariable int id, @Nullable @RequestParam String sort) {
		return ApiResult.ok();
	}

	@PostMapping("/{transId}/like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "点赞文章")
	public ApiResult<Boolean> likeTrans(@RequestAttribute User user, @PathVariable int transId) {
		return ApiResult.ok();
	}

	@PostMapping("/{transId}/cancel_like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "取消点赞文章")
	public ApiResult<Boolean> cancelLikeTrans(@RequestAttribute User user, @PathVariable int transId) {
		return ApiResult.ok();
	}

	@PostMapping("/detail/comment/add")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "添加对翻译片段的评论")
	public ApiResult<Integer> addDetailTranslationComment(@RequestAttribute(value = "user") User user, @RequestBody AddTranslationSegCommentParam param) {
		return ApiResult.ok();
	}

	@PostMapping("/detail/comment/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除对翻译片段的评论")
	public ApiResult<Boolean> deleteDetailTranslationComment(@PathVariable int id) {
		return ApiResult.ok();
	}

	@PostMapping("/detail/{transSegId}/like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "点赞文章")
	public ApiResult<Boolean> likeTransSeg(@RequestAttribute User user, @PathVariable int transSegId) {
		return ApiResult.ok();
	}

	@PostMapping("/detail/{transSegId}/cancel_like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "取消点赞文章")
	public ApiResult<Boolean> cancelLikeTransSeg(@RequestAttribute User user, @PathVariable int transSegId) {
		return ApiResult.ok();
	}

	@GetMapping("/detail/{id}/comments")
	@ResponseBody
	@ApiOperation(value = "获取单句翻译的评论")
	public ApiResult<List<ArticleCommentVo>> getDetailTranslationComment(@RequestParam @Min(1) int pageId, @RequestParam @Min(1) int pageSize, @PathVariable int id, @Nullable @RequestParam String sort) {
		return ApiResult.ok();
	}

}
