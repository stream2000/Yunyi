package net.yunyi.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginEnable;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.TransComment;
import net.yunyi.back.persistence.entity.TransItemComment;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.AddTranslationCommentParam;
import net.yunyi.back.persistence.param.AddTranslationSegCommentParam;
import net.yunyi.back.persistence.param.UploadTransParam;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import net.yunyi.back.persistence.service.trans.ITransCommentService;
import net.yunyi.back.persistence.service.trans.ITransItemCommentService;
import net.yunyi.back.persistence.vo.SimpleTranslationVo;
import net.yunyi.back.persistence.vo.StatsVo;
import net.yunyi.back.persistence.vo.TransCommentPageVo;
import net.yunyi.back.persistence.vo.TransCommentVo;
import net.yunyi.back.persistence.vo.TransSegCommentPageVo;
import net.yunyi.back.persistence.vo.TransSegCommentVo;
import net.yunyi.back.persistence.vo.TranslationDetailVo;
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

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collections;

@RestController
@Validated
@RequestMapping("/trans")
public class TranslationController {

	@Autowired
	IArticleTransService articleTransService;

	@Autowired
	ITransCommentService transCommentService;

	@Autowired
	ITransItemCommentService transItemCommentService;

	@PostMapping("/upload")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "上传翻译接口, 返回翻译id")
	public ApiResult<Integer> uploadTranslation(@RequestAttribute("user") User user,
			@RequestBody @Valid UploadTransParam param) {
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
	public ApiResult<Integer> modifyTranslation(@RequestAttribute("user") User user, @PathVariable int id,
			@RequestBody @Valid UploadTransParam param) {
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
	public ApiResult<Integer> addTranslationComment(@RequestAttribute(value = "user") User user,
			@RequestBody @Valid AddTranslationCommentParam param) {
		return ApiResult.ok(transCommentService.addTransComment(user.getId().intValue(), param.getTransId(),
				param.getContent(), param.isHasRefComment(), param.getRefCommentId()));
	}

	@PostMapping("/comment/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除对整个翻译的评论")
	public ApiResult<Boolean> deleteTranslationComment(@PathVariable int id) {
		return ApiResult.ok(transCommentService.deleteArticleComment(id));
	}

	@GetMapping("/{id}/comments")
	@ResponseBody
	@ApiOperation(value = "获取翻译界面的评论")
	public ApiResult<TransCommentPageVo> getTranslationComment(@RequestParam @Min(1) int pageId,
			@RequestParam @Min(1) int pageSize, @PathVariable int id, @Nullable @RequestParam String sort) {
		QueryWrapper<TransCommentVo> query = new QueryWrapper<>();
		query.eq("c.trans_id", id);
		IPage<TransCommentVo> result = transCommentService.getTransComments(new Page<>(pageId, pageSize), query);

		int commentCount = transCommentService.count(new QueryWrapper<TransComment>().eq("trans_id", id));

		return ApiResult.ok(new TransCommentPageVo(result.getRecords(), commentCount));
	}

	@PostMapping("/{transId}/like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "点赞文章")
	public ApiResult<Boolean> likeTrans(@RequestAttribute User user, @PathVariable int transId) {
		return ApiResult.ok(articleTransService.likeTrans(transId, user.getId().intValue()));
	}

	@PostMapping("/{transId}/cancel_like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "取消点赞文章")
	public ApiResult<Boolean> cancelLikeTrans(@RequestAttribute User user, @PathVariable int transId) {
		return ApiResult.ok(articleTransService.cancelLikeTrans(transId, user.getId().intValue()));
	}

	@GetMapping("/{transId}/detail")
	@ResponseBody
	@LoginEnable
	@ApiOperation(value = "获取翻译详情(分段数据)")
	public ApiResult<TranslationDetailVo> getTranslationDetail(@Nullable @RequestAttribute(value = "user") User user,
			@PathVariable final int transId) {
		int userId = user == null ? -1 : user.getId().intValue();
		return ApiResult.ok(articleTransService.getTranslationDetail(userId, transId));
	}


	@GetMapping("/{transId}/stats")
	@ResponseBody
	@LoginEnable
	@ApiOperation(value = "获取翻译详情(分段数据)")
	public ApiResult<StatsVo> getTranslationStats(@Nullable @RequestAttribute(value = "user") User user,
			@PathVariable final int transId) {
		int userId = user == null ? -1 : user.getId().intValue();
		return ApiResult.ok(articleTransService.getTransStats(userId, transId));
	}


	@PostMapping("/detail/comment/add")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "添加对翻译片段的评论")
	public ApiResult<Integer> addDetailTranslationComment(@RequestAttribute(value = "user") User user,
			@RequestBody AddTranslationSegCommentParam param) {
		return ApiResult.ok(transItemCommentService.addTransItemComment(user.getId().intValue(), param.getTransSegId()
				, param.getContent()));
	}

	@PostMapping("/detail/comment/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除对翻译片段的评论")
	public ApiResult<Boolean> deleteDetailTranslationComment(@PathVariable int id) {
		return ApiResult.ok(transItemCommentService.deleteTransItemComment(id));
	}

	@PostMapping("/detail/{transSegId}/like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "点赞文章")
	public ApiResult<Boolean> likeTransSeg(@RequestAttribute User user, @PathVariable int transSegId) {
		return ApiResult.ok(articleTransService.likeTransSeg(user.getId().intValue(), transSegId));
	}

	@PostMapping("/detail/{transSegId}/cancel_like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "取消点赞文章")
	public ApiResult<Boolean> cancelLikeTransSeg(@RequestAttribute User user, @PathVariable int transSegId) {
		return ApiResult.ok(articleTransService.cancelLikeTransSeg(user.getId().intValue(), transSegId));
	}

	@GetMapping("/detail/{id}/comments")
	@ResponseBody
	@ApiOperation(value = "获取单句翻译的评论")
	public ApiResult<TransSegCommentPageVo> getDetailTranslationComment(@RequestParam @Min(1) int pageId,
			@RequestParam @Min(1) int pageSize, @PathVariable int id, @Nullable @RequestParam String sort) {
		IPage<TransSegCommentVo> result = transItemCommentService.getTransSegComments(new Page<>(pageId, pageSize),
				id);
		int commentCount = transItemCommentService.count(new QueryWrapper<TransItemComment>().eq("trans_seg_id", id));
		return ApiResult.ok(new TransSegCommentPageVo(result.getRecords(), commentCount));
	}

	@GetMapping("/seg/{id}/stats")
	@ResponseBody
	@ApiOperation(value = "获取单句翻译的评论")
	@LoginEnable
	public ApiResult<StatsVo> getDetailTranslationComment(@Nullable @RequestAttribute User user,
			@PathVariable int id) {
		int userId = user == null ? -1 : user.getId().intValue();
		return ApiResult.ok(articleTransService.getTransSegStats(userId, id));
	}
}
