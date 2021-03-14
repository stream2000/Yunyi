package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.TransItemComment;
import net.yunyi.back.persistence.mapper.TransItemCommentMapper;
import net.yunyi.back.persistence.service.trans.ITransItemCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 单句评论表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class TransItemCommentServiceImpl extends ServiceImpl<TransItemCommentMapper, TransItemComment> implements ITransItemCommentService {

	@Override
	@Transactional
	public int addTransItemComment(final int senderId, final int transSegId, final String content) {
		TransItemComment comment = new TransItemComment();
		comment.setContent(content);
		comment.setSenderId(senderId);
		comment.setTransSegId(transSegId);
		int floor = count(new QueryWrapper<TransItemComment>().eq("transSegId", transSegId)) + 1;
		comment.setFloor(floor);
		save(comment);
		return comment.getId().intValue();
	}

	@Override
	public boolean deleteTransItemComment(final int transItemId) {
		return removeById(transItemId);
	}


}
