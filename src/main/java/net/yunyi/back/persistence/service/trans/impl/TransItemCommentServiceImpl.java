package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.TransItemComment;
import net.yunyi.back.persistence.entity.TransSegStats;
import net.yunyi.back.persistence.mapper.TransItemCommentMapper;
import net.yunyi.back.persistence.service.trans.ITransItemCommentService;
import net.yunyi.back.persistence.service.trans.ITransSegStatsService;
import net.yunyi.back.persistence.vo.TransSegCommentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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

	Logger logger = LoggerFactory.getLogger(TransItemCommentServiceImpl.class);

	@Autowired
	ITransSegStatsService transSegStatsService;

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

		TransSegStats stats = transSegStatsService.getById(transSegId);
		if (stats == null) {
			logger.error("trans seg stats with id {} not exists", transSegId);
			throw new BizException(YunyiCommonEnum.TRANS_SEG_STATS_NOT_EXISTS);
		}

		stats.setCommentNum(stats.getCommentNum() + 1);
		transSegStatsService.updateById(stats);

		return comment.getId().intValue();
	}

	@Override
	public boolean deleteTransItemComment(final int transItemCommentId) {
		TransItemComment comment = getById(transItemCommentId);
		if (comment == null) {
			logger.error("trans seg comment with id {} not exists", transItemCommentId);
			throw new BizException(YunyiCommonEnum.TRANS_SEG_NOT_EXISTS);
		}
		boolean ok = removeById(transItemCommentId);

		TransSegStats stats = transSegStatsService.getById(comment.getTransSegId());
		if (stats == null) {
			logger.error("trans seg stats with id {} not exists", comment.getTransSegId());
			throw new BizException(YunyiCommonEnum.TRANS_SEG_STATS_NOT_EXISTS);
		}
		stats.setCommentNum(stats.getCommentNum() - 1);
		transSegStatsService.updateById(stats);

		return ok;
	}

	@Override
	public IPage<TransSegCommentVo> getTransSegComments(final Page<TransSegCommentVo> page, final int transSegId) {
		IPage<TransSegCommentVo> result = baseMapper.getTransSegComments(page, new QueryWrapper<TransSegCommentVo>().eq("trans_seg_id", transSegId));
		result.setRecords(result.getRecords().stream().sorted().collect(Collectors.toList()));
		return result;
	}

}
