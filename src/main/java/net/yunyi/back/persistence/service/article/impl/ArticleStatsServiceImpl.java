package net.yunyi.back.persistence.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.ArticleStats;
import net.yunyi.back.persistence.mapper.ArticleStatsMapper;
import net.yunyi.back.persistence.service.article.IArticleStatsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-01
 */
@Service
public class ArticleStatsServiceImpl extends ServiceImpl<ArticleStatsMapper, ArticleStats> implements IArticleStatsService {

	@Override
	public ArticleStats getByArticleId(final int articleId) {
		return getOne(new QueryWrapper<ArticleStats>().eq("article_id", articleId));
	}
}
