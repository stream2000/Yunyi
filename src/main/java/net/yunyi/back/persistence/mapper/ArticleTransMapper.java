package net.yunyi.back.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import net.yunyi.back.persistence.entity.ArticleTrans;
import net.yunyi.back.persistence.vo.BestTranslationVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 原文切分的翻译表 Mapper 接口
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
public interface ArticleTransMapper extends BaseMapper<ArticleTrans> {

	BestTranslationVo getBestTranslation(@Param(Constants.WRAPPER) Wrapper<ArticleTrans> ew);
}
