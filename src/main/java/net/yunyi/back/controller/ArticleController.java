package net.yunyi.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.param.UploadArticleParam;
import net.yunyi.back.persistence.service.IArticleService;
import net.yunyi.back.persistence.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	IArticleService articleService;

	@GetMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "获取首页文章接口, 带分页")
	public ApiResult<ArticleVo> getArticleById(@PathVariable int id) {
		ArticleVo article = articleService.getArticleByQuery(new QueryWrapper<ArticleVo>().eq("a.id", id));
		return ApiResult.ok(article);
	}

	@GetMapping("/all")
	@ResponseBody
	@ApiOperation(value = "获取首页文章接口, 带分页")
	public ApiResult<List<ArticleVo>> getArticles(@RequestParam @Min(1) int pageId, @RequestParam @Min(1) int pageSize) {
		IPage<ArticleVo> articles = articleService.getArticles(new Page<>(pageId, pageSize));
		return ApiResult.ok(articles.getRecords());
	}

	@PostMapping("/like/{articleId}")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "点赞文章")
	public ApiResult<Boolean> likeArticle(@RequestAttribute User user, @PathVariable int articleId) {
		return ApiResult.ok(articleService.likeArticle(articleId, user.getId().intValue()));
	}

	@PostMapping("/cancel_like/{articleId}")
	@ResponseBody
	@LoginRequired
	@ApiOperation(value = "取消点赞文章")
	public ApiResult<Boolean> cancelLikeArticle(@RequestAttribute User user, @PathVariable int articleId) {
		return ApiResult.ok(articleService.cancelLikeArticle(articleId, user.getId().intValue()));
	}

	@PostMapping("/upload")
	@ResponseBody
	@LoginRequired
	public ApiResult<Article> uploadArticle(@RequestBody UploadArticleParam param, @RequestAttribute("user") User user) {
		Article article = articleService.addArticle(user.getId().intValue(), param.getTitle(), param.getOriginalText(), param.getGenre());
		return ApiResult.ok(article);
	}

	@PostMapping("/modify")
	@ResponseBody
	@LoginRequired
	public ApiResult<Article> modifyArticle(@RequestBody UploadArticleParam param, @RequestAttribute("user") User user) {
		// TODO: add more validation here. For example, only member can modify the article
		Article article = articleService.modifyArticle(param.getArticleId(), param.getTitle(), param.getTransTitle(), param.getOriginalText(), param.getGenre());
		if (article == null) {
			return ApiResult.error(YunyiCommonEnum.ARTICLE_NOT_FOUND);
		}
		return ApiResult.ok(article);
	}

	@PostMapping("/{articleId}/request_trans")
	@ResponseBody
	@LoginRequired
	public ApiResult<Article> requestTrans(@PathVariable int articleId, @RequestAttribute("user") User user) {
		return ApiResult.ok();
	}
}

