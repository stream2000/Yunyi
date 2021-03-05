package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.ArticleLike;
import net.yunyi.back.persistence.entity.ArticleStats;
import net.yunyi.back.persistence.entity.RequestTrans;
import net.yunyi.back.persistence.mapper.ArticleMapper;
import net.yunyi.back.persistence.service.IArticleLikeService;
import net.yunyi.back.persistence.service.IArticleService;
import net.yunyi.back.persistence.service.IArticleStatsService;
import net.yunyi.back.persistence.service.IRequestTransService;
import net.yunyi.back.persistence.vo.ArticleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
	@Autowired
	IArticleLikeService articleLikeService;

	@Autowired
	IArticleStatsService articleStatsService;

	@Autowired
	IRequestTransService requestTransService;

	private static QueryWrapper<ArticleLike> queryLikeTableById(int articleId, int userId) {
		return new QueryWrapper<ArticleLike>().eq("article_id", articleId).eq("user_id", userId);
	}

	// TODO: store texts after splitting the article
	@Override
	@Transactional
	public Article addArticle(final int uploaderId, final String title, final String originalText, final String genre) {
		Article article = new Article();
		article.setGenre(genre);
		article.setUploaderId(uploaderId);
		article.setTitle(title);
		article.setOriginalText(originalText);
		save(article);
		ArticleStats stats = new ArticleStats();
		stats.setArticleId(article.getId());
		articleStatsService.save(stats);
		return article;
	}

	@Override
	public Article modifyArticle(final int articleId, final String title, final String transTitle, final String originalText, final String genre) {
		Article article = getById(articleId);
		if (article == null) {
			return null;
		}
		if (StringUtils.isNotBlank(genre)) {
			article.setGenre(genre);
		}
		if (StringUtils.isNotBlank(title)) {
			article.setTitle(title);
		}
		if (StringUtils.isNotBlank(transTitle)) {
			article.setTransTitle(transTitle);
		}
		if (StringUtils.isNotEmpty(originalText)) {
			article.setOriginalText(originalText);
		}
		updateById(article);
		return article;
	}

	@Override
	@Transactional
	public boolean requestTrans(final int articleId, final int userId) {
		QueryWrapper<RequestTrans> query = new QueryWrapper<RequestTrans>().eq("user_id", userId).eq("article_id", articleId);
		if (requestTransService.getOne(query) != null) {
			return true;
		}
		RequestTrans requestTrans = new RequestTrans();
		requestTrans.setArticleId(articleId);
		requestTrans.setUserId(userId);
		requestTrans.setSolved(false);
		requestTransService.save(requestTrans);
		ArticleStats stats = articleStatsService.getById(articleId);
		// todo: concurrent issue
		int currentRequestTransNum = stats.getTransRequestNum() != null ? stats.getTransRequestNum() : 0;
		stats.setTransRequestNum(currentRequestTransNum + 1);
		articleStatsService.updateById(stats);
		return true;
	}

	@Override
	public IPage<ArticleVo> getArticles(Page<ArticleVo> page) {
		return baseMapper.getAllArticles(page);
	}

	@Override
	public ArticleVo getArticleByQuery(final QueryWrapper<ArticleVo> queryWrapper) {
		return baseMapper.getArticleByQuery(queryWrapper);
	}

	@Override
	@Transactional
	public boolean likeArticle(final int articleId, final int userId) {
		QueryWrapper<ArticleLike> query = queryLikeTableById(articleId, userId);
		if (articleLikeService.getOne(query) != null) {
			return true;
		}
		ArticleLike like = new ArticleLike();
		like.setArticleId(articleId);
		like.setUserId(userId);
		articleLikeService.save(like);
		ArticleStats stats = articleStatsService.getById(articleId);
		// todo: concurrent issue
		int currentLikeNum = stats.getLikeNum() != null ? stats.getLikeNum() : 0;
		stats.setLikeNum(currentLikeNum + 1);
		articleStatsService.updateById(stats);
		return true;
	}

	@Override
	@Transactional
	public boolean cancelLikeArticle(final int articleId, final int userId) {
		QueryWrapper<ArticleLike> query = queryLikeTableById(articleId, userId);
		if (articleLikeService.getOne(query) == null) {
			return false;
		}
		articleLikeService.remove(queryLikeTableById(articleId, userId));
		ArticleStats stats = articleStatsService.getById(articleId);
		int currentLikeNum = stats.getLikeNum() != null ? stats.getLikeNum() : 1;
		stats.setLikeNum(currentLikeNum - 1);
		articleStatsService.updateById(stats);
		return true;
	}
}
