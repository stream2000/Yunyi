package net.yunyi.back.persistence.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.ArticleComment;
import net.yunyi.back.persistence.vo.ArticleCommentVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-06
 */
public interface IArticleCommentService extends IService<ArticleComment> {
	ArticleCommentVo addArticleComment(int senderId, int articleId, String content, boolean hasRefComment, int refCommentId);

	boolean deleteArticleComment(final int commentId);

	IPage<ArticleCommentVo> getArticleComments(Page<ArticleCommentVo> page, int articleId);
}
