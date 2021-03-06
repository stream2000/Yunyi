package net.yunyi.back.persistence.service.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.vo.ArticleVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
public interface IArticleService extends IService<Article> {

	Article addArticle(int uploaderId, String title, String originalText, String genre);

	Article modifyArticle(int articleId, String title, String transTitle, String originalText, String genre);

	boolean deleteArticle(int articleId);

	boolean requestTrans(int articleId, int userId);

	IPage<ArticleVo> getArticles(Page<ArticleVo> page);

	ArticleVo getArticleByQuery(QueryWrapper<ArticleVo> queryWrapper);

	boolean likeArticle(int articleId, final int userId);

	boolean cancelLikeArticle(int articleId, final int userId);

}
