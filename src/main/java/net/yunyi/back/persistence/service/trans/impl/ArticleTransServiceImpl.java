package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.entity.ArticleSegTrans;
import net.yunyi.back.persistence.entity.ArticleTrans;
import net.yunyi.back.persistence.entity.TransStats;
import net.yunyi.back.persistence.mapper.ArticleTransMapper;
import net.yunyi.back.persistence.param.UploadTransParam;
import net.yunyi.back.persistence.service.IArticleSegTransService;
import net.yunyi.back.persistence.service.ITransLikeService;
import net.yunyi.back.persistence.service.ITransStatsService;
import net.yunyi.back.persistence.service.article.IArticleService;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

		for (int i = 0; i < param.getSegmentList().size(); i++) {
			UploadTransParam.TransSegment transSegment = param.getSegmentList().get(i);
			ArticleSegTrans segTrans = new ArticleSegTrans();
			segTrans.setTransSeq(i);
			segTrans.setTransId(articleTrans.getId().intValue());
			segTrans.setContent(transSegment.getTranslationContent());
			articleSegTransService.save(segTrans);
		}

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

}
