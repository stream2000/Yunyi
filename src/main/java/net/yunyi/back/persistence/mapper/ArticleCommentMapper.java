package net.yunyi.back.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.yunyi.back.persistence.entity.ArticleComment;
import net.yunyi.back.persistence.vo.ArticleCommentVo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author stream2000
 * @since 2021-03-06
 */
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

	IPage<ArticleCommentVo> getArticleComments(Page<?> page, int articleId);

	ArticleCommentVo getArticleComment(int commentId);
}