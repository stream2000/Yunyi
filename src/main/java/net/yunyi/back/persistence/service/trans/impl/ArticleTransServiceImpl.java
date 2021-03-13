package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.ArticleSegTrans;
import net.yunyi.back.persistence.entity.ArticleTrans;
import net.yunyi.back.persistence.entity.TransStats;
import net.yunyi.back.persistence.mapper.ArticleTransMapper;
import net.yunyi.back.persistence.param.UploadTransParam;
import net.yunyi.back.persistence.service.article.IArticleService;
import net.yunyi.back.persistence.service.trans.IArticleSegTransService;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import net.yunyi.back.persistence.service.trans.ITransLikeService;
import net.yunyi.back.persistence.service.trans.ITransStatsService;
import net.yunyi.back.persistence.vo.ArticleListItemVo;
import net.yunyi.back.persistence.vo.SimpleTranslationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 原文切分的翻译表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class ArticleTransServiceImpl extends ServiceImpl<ArticleTransMapper, ArticleTrans> implements IArticleTransService {
	@Autowired
	IArticleSegTransService articleSegTransService;

	@Autowired
	ITransLikeService transLikeService;

	@Autowired
	ITransStatsService transStatsService;

	@Autowired
	IArticleService articleService;

	@Override
	@Transactional
	public int uploadTranslation(final int userId, final UploadTransParam param) {
		ArticleTrans articleTrans = new ArticleTrans();
		articleTrans.setArticleId(param.getArticleId());
		articleTrans.setUploaderId(userId);
		save(articleTrans);

		saveTranslation(articleTrans.getId().intValue(), param);

		Article article = articleService.getById(param.getArticleId());
		if (article == null) {
			throw new BizException(YunyiCommonEnum.TRANS_ARTICLE_NOT_EXISTS);
		}

		if (article.getHasTrans() == null || !article.getHasTrans()) {
			article.setHasTrans(true);
			articleService.updateById(article);
		}

		TransStats transStats = new TransStats();
		transStats.setTransId(articleTrans.getId().intValue());
		transStats.setCommentNum(0);
		transStats.setLikeNum(0);
		transStatsService.save(transStats);

		return articleTrans.getId().intValue();
	}

	@Override
	public int modifyTranslation(final int transId, final UploadTransParam param) {
		ArticleTrans articleTrans = getById(transId);
		if (articleTrans == null) {
			throw new BizException(YunyiCommonEnum.TRANS_NOT_EXIST);
		}

		articleSegTransService.remove(new QueryWrapper<ArticleSegTrans>().eq("trans_id", transId));
		saveTranslation(transId, param);

		return articleTrans.getId().intValue();
	}

	@Override
	@Transactional
	public boolean deleteTranslation(final int userId, final int transId) {
		ArticleTrans trans = getById(transId);
		if (trans == null) {
			throw new BizException(YunyiCommonEnum.TRANS_NOT_EXIST);
		}

		Article article = articleService.getById(trans.getArticleId());
		if (article == null) {
			throw new BizException(YunyiCommonEnum.TRANS_ARTICLE_NOT_EXISTS);
		}

		if (trans.getUploaderId() != userId || !trans.getUploaderId().equals(article.getUploaderId())) {
			throw new BizException(YunyiCommonEnum.NO_PERMISSION_TO_DELETE);
		}

		removeById(transId);
		transStatsService.remove(new QueryWrapper<TransStats>().eq("trans_id", transId));
		int transCount = count(new QueryWrapper<ArticleTrans>().eq("article_id", trans.getArticleId()));
		if (transCount == 0) {
			article.setHasTrans(false);
			articleService.updateById(article);
		}

		articleSegTransService.remove(new QueryWrapper<ArticleSegTrans>().eq("trans_id", transId));

		// TODO delete comment and like
		return true;
	}

	@Override
	public SimpleTranslationVo getTranslation(final int transId) {
		SimpleTranslationVo vo = baseMapper.getBestTranslation(new QueryWrapper<ArticleTrans>().eq("trans_id", transId));
		vo.setContent(getSimpleTransContent(transId));
		return vo;
	}

	@Override
	@Transactional
	public void fillBestTranslationForArticle(final ArticleListItemVo vo) {
		QueryWrapper<ArticleTrans> bestTranslationQuery = new QueryWrapper<>();
		bestTranslationQuery.eq("article_id", vo.getId());
		bestTranslationQuery.orderByDesc("stats.like_num * 3 + stats.comment_num * 4");

		SimpleTranslationVo bestTranslation = baseMapper.getBestTranslation(bestTranslationQuery);
		if (bestTranslation == null) {
			return;
		}

		bestTranslation.setContent(getSimpleTransContent(bestTranslation.getTransId()));
		vo.setBestTranslation(bestTranslation);
	}

	@Override
	public void fillTranslations(final ArticleListItemVo vo) {
		QueryWrapper<ArticleTrans> query = new QueryWrapper<>();
		query.eq("article_id", vo.getId());
		query.orderByDesc("stats.like_num * 3 + stats.comment_num * 4");

		vo.setTranslations(baseMapper.getSimpleTranslations(query));
	}

	private String getSimpleTransContent(int transId) {
		List<ArticleSegTrans> trans = articleSegTransService.list(new QueryWrapper<ArticleSegTrans>().eq("trans_id", transId).orderByAsc("trans_seq"));
		String content = trans.stream().map(ArticleSegTrans::getContent).collect(Collectors.joining(""));
		int length = Math.min(content.length(), 300);
		return content.substring(0, length);
	}

	private void saveTranslation(int transId, UploadTransParam param) {
		for (int i = 0; i < param.getSegmentList().size(); i++) {
			UploadTransParam.TransSegment transSegment = param.getSegmentList().get(i);
			ArticleSegTrans segTrans = new ArticleSegTrans();
			segTrans.setTransSeq(i);
			segTrans.setTransId(transId);
			segTrans.setContent(transSegment.getTranslationContent());
			articleSegTransService.save(segTrans);
		}
	}

}
