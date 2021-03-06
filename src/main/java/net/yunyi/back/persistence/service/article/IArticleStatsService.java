package net.yunyi.back.persistence.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.ArticleStats;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-01
 */
public interface IArticleStatsService extends IService<ArticleStats> {
	ArticleStats getByArticleId(int articleId);
}
