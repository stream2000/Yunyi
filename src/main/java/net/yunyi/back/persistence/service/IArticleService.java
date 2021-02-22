package net.yunyi.back.persistence.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.vo.ArticleVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
public interface IArticleService extends IService<Article> {

    IPage<ArticleVo> getArticle(Page<ArticleVo> page, QueryWrapper<ArticleVo> wrapper);
}
