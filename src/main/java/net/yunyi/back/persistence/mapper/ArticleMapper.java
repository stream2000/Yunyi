package net.yunyi.back.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.vo.ArticleVo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<ArticleVo> getArticleInfo(Page<?> page, QueryWrapper<ArticleVo> ew);
}
