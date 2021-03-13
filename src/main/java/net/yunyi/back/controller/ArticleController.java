package net.yunyi.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.AddArticleCommentParam;
import net.yunyi.back.persistence.param.UploadArticleParam;
import net.yunyi.back.persistence.service.article.IArticleCommentService;
import net.yunyi.back.persistence.service.article.IArticleService;
import net.yunyi.back.persistence.vo.ArticleCommentVo;
import net.yunyi.back.persistence.vo.ArticleListItemVo;
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
	private static final String SORT_BY_NEWEST = "NEWEST";
	private static final String SORT_BY_DEFAULT = "DEFAULT";

	@Autowired
	IArticleService articleService;

	@Autowired
	IArticleCommentService articleCommentService;

	@GetMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "获取单个文章的具体内容, 在文章界面使用")
	public ApiResult<ArticleListItemVo> getArticleById(@PathVariable int id) {
		ArticleListItemVo article = articleService.getArticleByQuery(new QueryWrapper<ArticleListItemVo>().eq("a.id", id));
		return ApiResult.ok(article);
	}

	@GetMapping("/all")
	@ResponseBody
	@ApiOperation(value = "获取首页文章数据, 支持按热度排序（sort=hot)，按类别分类，带分页")
	public ApiResult<NewsPageVo> getArticles(@RequestParam @Min(1) int pageId, @RequestParam @Min(1) int pageSize, @Nullable @RequestParam final String genre, @RequestParam @Nullable String sort) {
		IPage<ArticleListItemVo> articles;
		int articleCount;
		// construct the query
		QueryWrapper<ArticleListItemVo> query = new QueryWrapper<>();
		QueryWrapper<Article> countQuery = new QueryWrapper<>();

		// filter by genre
		if (StringUtils.isBlank(genre) || genre.equalsIgnoreCase("all")) {
			articleCount = articleService.count();
		} else {
			query.eq("a.genre", query);
			countQuery.eq("genre", genre);
		}

		if (StringUtils.isBlank(sort)) {
			sort = "default";
		}

		// sort by certain method
		// TODO: more sort method
		switch (sort.toUpperCase()) {
		case SORT_BY_HOT:
			query.orderByDesc("stats.trans_request_num * 2 + stats.view_num + stats.comment_num * 3 + stats.like_num * 2");
			break;
		case SORT_BY_NEWEST:
			query.orderByAsc("UNIX_TIMESTAMP(a.create_time)");
			break;
		default:
			query.orderByDesc("UNIX_TIMESTAMP(a.create_time)");
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
	public ApiResult<Article> uploadArticle(@RequestBody UploadArticleParam param, @RequestAttribute("user") User user) {
		if (param.getSegments() == null || param.getSegments().isEmpty()) {
			throw new BizException(YunyiCommonEnum.ARTICLE_SEG_EMPTY);
		}
		Article article = articleService.addArticle(user.getId().intValue(), param.getTitle(), param.getGenre(), param.getSegments());
		return ApiResult.ok(article);
	}

	@PostMapping("/modify")
	@ResponseBody
	@ApiOperation(value = "修改文章")
	@LoginRequired
	public ApiResult<Article> modifyArticle(@RequestBody UploadArticleParam param, @RequestAttribute("user") User user) {
		// TODO: add more validation here. For example, only member can modify the article
		if (param.getSegments() == null || param.getSegments().isEmpty()) {
			throw new BizException(YunyiCommonEnum.ARTICLE_SEG_EMPTY);
		}
		Article article = articleService.modifyArticle(param.getArticleId(), param.getTitle(), param.getTransTitle(), param.getGenre(), param.getSegments());
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


	@PostMapping("/comment/add")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "添加原文页面评论")
	public ApiResult<ArticleCommentVo> addArticleComment(@RequestAttribute(value = "user") User user, @RequestBody AddArticleCommentParam param) {
		return ApiResult.ok(articleCommentService.addArticleComment(user.getId().intValue(), param.getArticleId(), param.getContent(), param.isHasRefComment(), param.getRefCommentId()));
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
	public ApiResult<List<ArticleCommentVo>> getArticleComment(@RequestParam @Min(1) int pageId, @RequestParam @Min(1) int pageSize, @PathVariable int id, @Nullable @RequestParam String sort) {
		return ApiResult.ok(articleCommentService.getArticleComment(new Page<>(pageId, pageSize), id).getRecords().stream().sorted().collect(Collectors.toList()));
	}

}

