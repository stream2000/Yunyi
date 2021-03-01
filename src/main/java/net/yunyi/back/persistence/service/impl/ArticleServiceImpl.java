package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.Article;
import net.yunyi.back.persistence.mapper.ArticleMapper;
import net.yunyi.back.persistence.service.IArticleService;
import net.yunyi.back.persistence.vo.ArticleVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

	@Override
	public IPage<ArticleVo> getArticle(Page<ArticleVo> page, QueryWrapper<ArticleVo> wrapper) {
		return baseMapper.getArticleInfo(page, wrapper);
	}
}
