package net.yunyi.back.persistence.service.trans;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.TransComment;
import net.yunyi.back.persistence.vo.TransCommentVo;

/**
 * <p>
 * 翻译评论表 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
public interface ITransCommentService extends IService<TransComment> {
	int addTransComment(int senderId, int transId, String content, boolean hasRefComment, int refCommentId);

	boolean deleteArticleComment(final int commentId);

	IPage<TransCommentVo> getTransComments(Page<TransCommentVo> page, QueryWrapper<TransCommentVo> query);
}
