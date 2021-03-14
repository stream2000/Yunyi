package net.yunyi.back.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.yunyi.back.persistence.entity.TransItemComment;
import net.yunyi.back.persistence.vo.TransSegCommentVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 单句评论表 Mapper 接口
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
public interface TransItemCommentMapper extends BaseMapper<TransItemComment> {
	IPage<TransSegCommentVo> getTransSegComments(Page<?> page, QueryWrapper<TransSegCommentVo> ew);

	TransSegCommentVo getTransSegComment(@Param(Constants.WRAPPER) Wrapper<TransSegCommentVo> ew);
}
