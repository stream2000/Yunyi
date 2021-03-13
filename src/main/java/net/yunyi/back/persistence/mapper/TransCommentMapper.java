package net.yunyi.back.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.yunyi.back.persistence.entity.TransComment;
import net.yunyi.back.persistence.vo.TransCommentVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 翻译评论表 Mapper 接口
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
public interface TransCommentMapper extends BaseMapper<TransComment> {

	IPage<TransCommentVo> getTransComments(Page<?> page, QueryWrapper<TransCommentVo> ew);

	TransCommentVo getTransComment(@Param(Constants.WRAPPER) Wrapper<TransCommentVo> ew);
}
