package net.yunyi.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.persistence.service.IArticleService;
import net.yunyi.back.persistence.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class ArticleController {

	@Autowired
	IArticleService articleService;

	@GetMapping("/")
	@ResponseBody
	@ApiOperation(value = "获取文章接口")
	public ApiResult<List<ArticleVo>> getArticle() {
		IPage<ArticleVo> articles = articleService.getArticle(new Page<>(1, 10), new QueryWrapper<ArticleVo>().le("u.id", 5));
		return ApiResult.ok(articles.getRecords());
	}
}

