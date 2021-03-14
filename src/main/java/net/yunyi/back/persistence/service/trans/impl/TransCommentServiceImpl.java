package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.TransComment;
import net.yunyi.back.persistence.entity.TransStats;
import net.yunyi.back.persistence.mapper.TransCommentMapper;
import net.yunyi.back.persistence.service.trans.ITransCommentService;
import net.yunyi.back.persistence.service.trans.ITransStatsService;
import net.yunyi.back.persistence.vo.RefTransCommentVo;
import net.yunyi.back.persistence.vo.TransCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * <p>
 * 翻译评论表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class TransCommentServiceImpl extends ServiceImpl<TransCommentMapper, TransComment> implements ITransCommentService {
	@Autowired
	ITransStatsService transStatsService;

	private RefTransCommentVo mapTransCommentToRef(TransCommentVo vo) {
		if (vo == null) {
			return null;
		}
		return new RefTransCommentVo(vo.getId(), vo.getSenderId(), vo.getSenderName(), vo.getContent(), vo.getSendTime(), vo.getFloor(), vo.isHasRefComment(), vo.getRefCommentId());
	}

	private int getTransCommentFloor(int articleId) {
		return count(new QueryWrapper<TransComment>().eq("trans_id", articleId)) + 1;
	}

	@Override
	@Transactional
	public int addTransComment(final int senderId, final int transId, final String content, final boolean hasRefComment, final int refCommentId) {
		if (hasRefComment) {
			TransComment refComment = getById(refCommentId);
			if (refComment == null) {
				throw new BizException(YunyiCommonEnum.REF_COMMENT_NOT_FOUND);
			}
		}

		TransComment comment = new TransComment();
		comment.setTransId(transId);
		comment.setHasRefComment(hasRefComment);
		comment.setRefCommentId(refCommentId);
		comment.setContent(content);
		comment.setSenderId(senderId);
		comment.setFloor(getTransCommentFloor(transId));
		save(comment);

		TransStats stats = transStatsService.getById(transId);
		stats.setCommentNum(stats.getCommentNum() + 1);
		transStatsService.updateById(stats);
		return comment.getId().intValue();
	}

	@Override
	public boolean deleteArticleComment(final int commentId) {
		TransComment comment = getById(commentId);
		boolean result = removeById(commentId);
		if (!result) {
			return false;
		}
		TransStats stats = transStatsService.getById(comment.getTransId());
		stats.setCommentNum(stats.getCommentNum() - 1);
		transStatsService.updateById(stats);
		return true;
	}

	@Override
	public IPage<TransCommentVo> getTransComments(final Page<TransCommentVo> page, final QueryWrapper<TransCommentVo> query) {
		IPage<TransCommentVo> result = baseMapper.getTransComments(page, query);
		for (TransCommentVo commentVo : result.getRecords()) {
			if (commentVo.isHasRefComment()) {
				TransCommentVo refComment = baseMapper.getTransComment(new QueryWrapper<TransCommentVo>().eq("c.id", commentVo.getRefCommentId()));
				commentVo.setRefComment(mapTransCommentToRef(refComment));
			}
		}
		result.setRecords(result.getRecords().stream().sorted().collect(Collectors.toList()));
		return result;
	}
}
