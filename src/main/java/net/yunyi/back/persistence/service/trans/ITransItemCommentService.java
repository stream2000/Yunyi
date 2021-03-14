package net.yunyi.back.persistence.service.trans;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.TransItemComment;
import net.yunyi.back.persistence.vo.TransSegCommentVo;

/**
 * <p>
 * 单句评论表 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
public interface ITransItemCommentService extends IService<TransItemComment> {
	int addTransItemComment(int senderId, int transSegId, String content);

	boolean deleteTransItemComment(int transItemCommentId);

	IPage<TransSegCommentVo> getTransSegComments(Page<TransSegCommentVo> page, int transSegId);

}
