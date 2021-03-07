package net.yunyi.back.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.vo.ArticleListItemVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
public interface ArticleMapper extends BaseMapper<Article> {

	IPage<ArticleListItemVo> getAllArticles(Page<?> page, @Param(Constants.WRAPPER) Wrapper<ArticleListItemVo> ew);

	ArticleListItemVo getArticleByQuery(@Param(Constants.WRAPPER) Wrapper<ArticleListItemVo> ew);
}
