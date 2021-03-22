package net.yunyi.back.persistence.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.ArticleLike;
import net.yunyi.back.persistence.entity.ArticleStats;
import net.yunyi.back.persistence.entity.ArticleTextSeg;
import net.yunyi.back.persistence.entity.RequestTrans;
import net.yunyi.back.persistence.mapper.ArticleMapper;
import net.yunyi.back.persistence.service.article.IArticleLikeService;
import net.yunyi.back.persistence.service.article.IArticleService;
import net.yunyi.back.persistence.service.article.IArticleStatsService;
import net.yunyi.back.persistence.service.article.IRequestTransService;
import net.yunyi.back.persistence.service.trans.IArticleTextSegService;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import net.yunyi.back.persistence.vo.ArticleListItemVo;
import net.yunyi.back.persistence.vo.ArticleTranslationVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

	@Autowired
	IArticleTextSegService articleTextSegService;

	@Autowired
	IArticleTransService transService;

	private static QueryWrapper<ArticleLike> queryLikeTableById(int articleId, int userId) {
		return new QueryWrapper<ArticleLike>().eq("article_id", articleId).eq("user_id", userId);
	}

	// TODO: store texts after splitting the article
	@Override
	@Transactional
	public Article addArticle(final int uploaderId, final String title, final String genre,
			final List<String> segments) {
		Article article = new Article();
		article.setGenre(genre);
		article.setUploaderId(uploaderId);
		article.setTitle(title);
		String text = String.join("", segments);
		article.setOriginalText(text);
		save(article);

		ArticleStats stats = new ArticleStats();
		stats.setArticleId(article.getId());
		stats.setLikeNum(0);
		stats.setTransRequestNum(0);
		stats.setCommentNum(0);
		stats.setViewNum(0);
		articleStatsService.save(stats);

		saveSegments(article.getId(), segments);
		return article;
	}

	@Override
	@Transactional
	public Article modifyArticle(final int articleId, final String title, final String transTitle, final String genre,
			final List<String> segments) {
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
		if (segments != null && !segments.isEmpty()) {
			article.setOriginalText(String.join("", segments));
			// TODO delete all relative records, but now is enough
			articleTextSegService.remove(new QueryWrapper<ArticleTextSeg>().eq("article_id", articleId));
			saveSegments(articleId, segments);
		}
		updateById(article);
		return article;
	}

	@Override
	@Transactional
	public boolean deleteArticle(final int articleId) {
		Article article = getById(articleId);
		if (article == null) {
			throw new BizException(YunyiCommonEnum.ARTICLE_NOT_FOUND.getResultCode(), "the article to delete not " +
					"found");
		}
		// delete the article
		removeById(articleId);
		// then delete the stat
		articleStatsService.removeById(articleId);
		articleLikeService.remove(new QueryWrapper<ArticleLike>().eq("article_id", articleId));
		requestTransService.remove(new QueryWrapper<RequestTrans>().eq("article_id", articleId));
		articleTextSegService.remove(new QueryWrapper<ArticleTextSeg>().eq("article_id", articleId));
		return true;
	}

	@Override
	@Transactional
	public boolean requestTrans(final int articleId, final int userId) {
		QueryWrapper<RequestTrans> query = new QueryWrapper<RequestTrans>().eq("user_id", userId).eq("article_id",
				articleId);
		if (requestTransService.getOne(query) != null) {
			return true;
		}
		RequestTrans requestTrans = new RequestTrans();
		requestTrans.setArticleId(articleId);
		requestTrans.setUserId(userId);
		requestTrans.setSolved(false);
		requestTransService.save(requestTrans);
		ArticleStats stats = articleStatsService.getById(articleId);
		int currentRequestTransNum = stats.getTransRequestNum() != null ? stats.getTransRequestNum() : 0;
		stats.setTransRequestNum(currentRequestTransNum + 1);
		articleStatsService.updateById(stats);
		return true;
	}

	@Override
	@Transactional
	public IPage<ArticleListItemVo> getArticlesByQuery(final Page<ArticleListItemVo> page,
			final QueryWrapper<ArticleListItemVo> query) {
		IPage<ArticleListItemVo> result = baseMapper.getAllArticles(page, query);
		result.getRecords().forEach(r -> {
			if (r != null && r.isHasTrans()) {
				transService.fillBestTranslationForArticle(r);
			}
		});
		return result;
	}

	@Override
	@Transactional
	public ArticleListItemVo getArticleByQuery(final QueryWrapper<ArticleListItemVo> queryWrapper, final int userId) {
		ArticleListItemVo vo = baseMapper.getArticleByQuery(queryWrapper);
		if (vo != null && vo.isHasTrans()) {
			transService.fillBestTranslationForArticle(vo);
			transService.fillTranslations(vo);
		}
		if (userId > 0) {
			vo.setLike(articleLikeService.getById(userId) != null);
			vo.setRequestVote(requestTransService.getById(userId) != null);
		}
		return vo;
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

	private void saveSegments(int articleId, List<String> segments) {
		for (int i = 0; i < segments.size(); i++) {
			String seg = segments.get(i);
			if (StringUtils.isBlank(seg)) {
				throw new BizException(YunyiCommonEnum.ARTICLE_SEG_EMPTY);
			}
			ArticleTextSeg articleTextSeg = new ArticleTextSeg();
			articleTextSeg.setArticleId(articleId);
			articleTextSeg.setContent(seg);
			articleTextSeg.setSequenceNumber(i);
			articleTextSegService.save(articleTextSeg);
		}
	}

	@Override
	@Transactional
	public ArticleTranslationVo getArticleTrans(final int articleId) {
		ArticleListItemVo articleListItemVo = new ArticleListItemVo();
		Article article = getById(articleId);
		if (article == null) {
			throw new BizException(YunyiCommonEnum.TRANS_ARTICLE_NOT_EXISTS);
		}
		if (article.getHasTrans() == null || !article.getHasTrans()) {
			return null;
		}
		articleListItemVo.setId(articleId);
		transService.fillTranslations(articleListItemVo);
		transService.fillBestTranslationForArticle(articleListItemVo);
		return new ArticleTranslationVo(articleListItemVo.getBestTranslation(), articleListItemVo.getTranslations());
	}
}
