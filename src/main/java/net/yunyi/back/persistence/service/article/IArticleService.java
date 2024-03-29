package net.yunyi.back.persistence.service.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.vo.ArticleListItemVo;
import net.yunyi.back.persistence.vo.ArticleTranslationVo;
import net.yunyi.back.persistence.vo.SimpleTranslationVo;
import net.yunyi.back.persistence.vo.UserUploadedArticleVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
public interface IArticleService extends IService<Article> {

	Article addArticle(int uploaderId, String title, String genre, final List<String> segments);

	Article modifyArticle(int articleId, String title, String transTitle, String genre, final List<String> segments);

	boolean deleteArticle(int articleId);

	boolean requestTrans(int articleId, int userId);

	IPage<ArticleListItemVo> getArticlesByQuery(Page<ArticleListItemVo> page, QueryWrapper<ArticleListItemVo> query);

	List<UserUploadedArticleVo> getArticlesByUserId(int userId);

	ArticleListItemVo getArticleByQuery(QueryWrapper<ArticleListItemVo> queryWrapper, final int userId);

	boolean likeArticle(int articleId, final int userId);

	boolean viewArticle(int articleId);

	boolean cancelLikeArticle(int articleId, final int userId);

	ArticleTranslationVo getArticleTrans(int articleId, final Page<SimpleTranslationVo> page);
}
