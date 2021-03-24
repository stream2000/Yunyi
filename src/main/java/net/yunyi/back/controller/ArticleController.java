package net.yunyi.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginEnable;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.ArticleComment;
import net.yunyi.back.persistence.entity.ArticleTextSeg;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.AddArticleCommentParam;
import net.yunyi.back.persistence.param.UploadArticleParam;
import net.yunyi.back.persistence.service.article.IArticleCommentService;
import net.yunyi.back.persistence.service.article.IArticleService;
import net.yunyi.back.persistence.service.trans.IArticleTextSegService;
import net.yunyi.back.persistence.vo.ArticleCommentPageVo;
import net.yunyi.back.persistence.vo.ArticleCommentVo;
import net.yunyi.back.persistence.vo.ArticleListItemVo;
import net.yunyi.back.persistence.vo.ArticleTranslationVo;
import net.yunyi.back.persistence.vo.NewsPageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
@Controller
@RequestMapping("/article")
@Validated
public class ArticleController {

	private static final String SORT_BY_HOT = "HOT";
	private static final String SORT_BY_CREATED = "CREATED";

	@Autowired
	IArticleService articleService;

	@Autowired
	IArticleCommentService articleCommentService;

	@Autowired
	IArticleTextSegService articleTextSegService;

	@GetMapping("/{id}")
	@ResponseBody
	@LoginEnable
	@ApiOperation(value = "获取单个文章的具体内容, 在文章界面使用")
	public ApiResult<ArticleListItemVo> getArticleById(@Nullable @RequestAttribute("user") User user,
			@PathVariable int id) {
		int userId = user == null ? 0 : user.getId().intValue();
		ArticleListItemVo article = articleService.getArticleByQuery(new QueryWrapper<ArticleListItemVo>().eq("a.id",
				id), userId);
		return ApiResult.ok(article);
	}

	@GetMapping("/all")
	@ResponseBody
	@ApiOperation(value = "获取首页文章数据, 带分页")
	@ApiImplicitParams({@ApiImplicitParam(name = "genre", value = "类别参数，直接传递中文"), @ApiImplicitParam(name = "sort",
			value = "hot: 热度 created: 发帖顺序"),

			@ApiImplicitParam(name = "hasTrans", value = "为空时不做过滤，有值时按值过滤"),

			@ApiImplicitParam(name = "token", value = "通过在参数中携带token，可以以登录状态获取该api，以得知是否已经点赞该文章")})
	public ApiResult<NewsPageVo> getArticles(@RequestParam @Min(1) int pageId, @RequestParam @Min(1) int pageSize,
			@Nullable @RequestParam final String genre, @RequestParam @Nullable String sort,
			@RequestParam @Nullable Boolean hasTrans) {
		IPage<ArticleListItemVo> articles;
		int articleCount;
		// construct the query
		QueryWrapper<ArticleListItemVo> query = new QueryWrapper<>();
		QueryWrapper<Article> countQuery = new QueryWrapper<>();

		// filter by genre
		if (StringUtils.isNotBlank(genre)) {
			query.eq("a.genre", genre);
			countQuery.eq("genre", genre);
		}

		if (StringUtils.isBlank(sort)) {
			sort = SORT_BY_HOT;
		}

		if (hasTrans != null) {
			if (hasTrans) {
				query.eq("a.has_trans", true);
				countQuery.eq("has_trans", true);
			} else {
				query.eq("a.has_trans", false).or().isNull("a.has_trans");
				countQuery.eq("has_trans", false).or().isNull("has_trans");
			}
		}

		// sort by certain method
		switch (sort.toUpperCase()) {
		case SORT_BY_CREATED:
			query.orderByDesc("UNIX_TIMESTAMP(a.create_time)");
			break;
		default:
			query.orderByDesc("stats.trans_request_num * 2 + stats.view_num + stats.comment_num * 3 + stats.like_num " + "*" + " 2");
			break;
		}

		// do the query
		articles = articleService.getArticlesByQuery(new Page<>(pageId, pageSize), query);
		articleCount = articleService.count(countQuery);

		// compute the page count
		int pageCount = articleCount / pageSize;
		if (pageCount == 0) {
			pageCount = 1;
		}

		return ApiResult.ok(new NewsPageVo(pageCount, articles.getRecords()));
	}

	@PostMapping("/{articleId}/like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "点赞文章")
	public ApiResult<Boolean> likeArticle(@RequestAttribute User user, @PathVariable int articleId) {
		return ApiResult.ok(articleService.likeArticle(articleId, user.getId().intValue()));
	}

	@PostMapping("/{articleId}/cancel_like")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "取消点赞文章")
	public ApiResult<Boolean> cancelLikeArticle(@RequestAttribute User user, @PathVariable int articleId) {
		return ApiResult.ok(articleService.cancelLikeArticle(articleId, user.getId().intValue()));
	}

	@PostMapping("/upload")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "上传文章")
	public ApiResult<Article> uploadArticle(@RequestBody UploadArticleParam param,
			@RequestAttribute("user") User user) {
		if (param.getSegments() == null || param.getSegments().isEmpty()) {
			throw new BizException(YunyiCommonEnum.ARTICLE_SEG_EMPTY);
		}
		Article article = articleService.addArticle(user.getId().intValue(), param.getTitle(), param.getGenre(),
				param.getSegments());
		return ApiResult.ok(article);
	}

	@PostMapping("/modify")
	@ResponseBody
	@ApiOperation(value = "修改文章, 目前的实现在已有翻译的情况比较triky，推荐在有翻译后禁止该api")
	@LoginRequired
	public ApiResult<Article> modifyArticle(@RequestBody UploadArticleParam param,
			@RequestAttribute("user") User user) {
		// TODO: add more validation here. For example, only member can modify the article
		if (param.getSegments() == null || param.getSegments().isEmpty()) {
			throw new BizException(YunyiCommonEnum.ARTICLE_SEG_EMPTY);
		}
		Article article = articleService.modifyArticle(param.getArticleId(), param.getTitle(), param.getTransTitle(),
				param.getGenre(), param.getSegments());
		if (article == null) {
			return ApiResult.error(YunyiCommonEnum.ARTICLE_NOT_FOUND);
		}
		return ApiResult.ok(article);
	}

	@PostMapping("/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除文章")
	public ApiResult<Boolean> removeArticle(@PathVariable int id, @RequestAttribute("user") User user) {
		// TODO: add more validation here. For example, only member can delete the article
		return ApiResult.ok(articleService.deleteArticle(id));
	}

	@PostMapping("/{articleId}/request_trans")
	@ResponseBody
	@ApiOperation(value = "请求翻译")
	@LoginRequired
	public ApiResult<Boolean> requestTrans(@PathVariable int articleId, @RequestAttribute("user") User user) {
		return ApiResult.ok(articleService.requestTrans(articleId, user.getId().intValue()));
	}

	@GetMapping("/{id}/segs")
	@ResponseBody
	@ApiOperation(value = "获取文章分段")
	public ApiResult<List<ArticleTextSeg>> getArticleTextSegs(@PathVariable int id) {

		QueryWrapper<ArticleTextSeg> query = new QueryWrapper<ArticleTextSeg>().eq("article_id", id);
		List<ArticleTextSeg> result = articleTextSegService.list(query);
		result.sort((o1, o2) -> o1.getSequenceNumber() > o2.getSequenceNumber() ? 1 : 0);

		return ApiResult.ok(result);
	}

	@PostMapping("/comment/add")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "添加原文页面评论")
	public ApiResult<ArticleCommentVo> addArticleComment(@RequestAttribute(value = "user") User user,
			@RequestBody AddArticleCommentParam param) {
		return ApiResult.ok(articleCommentService.addArticleComment(user.getId().intValue(), param.getArticleId(),
				param.getContent(), param.isHasRefComment(), param.getRefCommentId()));
	}

	@PostMapping("/comment/{id}/delete")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "删除对原文的评论")
	public ApiResult<Boolean> deleteArticleComment(@PathVariable int id) {
		return ApiResult.ok(articleCommentService.deleteArticleComment(id));
	}

	@GetMapping("/{id}/comments")
	@ResponseBody
	@ApiOperation(value = "获取翻译界面的评论")
	public ApiResult<ArticleCommentPageVo> getArticleComment(@RequestParam @Min(1) int pageId,
			@RequestParam @Min(1) int pageSize, @PathVariable int id, @Nullable @RequestParam String sort) {
		List<ArticleCommentVo> articleCommentVos = articleCommentService.getArticleComments(new Page<>(pageId,
				pageSize), id).getRecords().stream().sorted().collect(Collectors.toList());
		int commentCount = articleCommentService.count(new QueryWrapper<ArticleComment>().eq("article_id", id));

		// compute the page count
		int pageCount = commentCount / pageSize;
		if (pageCount == 0) {
			pageCount = 1;
		}
		return ApiResult.ok(new ArticleCommentPageVo(articleCommentVos, pageCount, commentCount));
	}

	@GetMapping("/{id}/trans")
	@ResponseBody
	@ApiOperation(value = "获取文章的翻译（最佳翻译+所有翻译的缩略）")
	public ApiResult<ArticleTranslationVo> getArticleTrans(@PathVariable int id) {
		return ApiResult.ok(articleService.getArticleTrans(id));
	}

}
