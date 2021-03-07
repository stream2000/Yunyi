package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.ArticleTextSeg;
import net.yunyi.back.persistence.mapper.ArticleTextSegMapper;
import net.yunyi.back.persistence.service.trans.IArticleTextSegService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 原文切分表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class ArticleTextSegServiceImpl extends ServiceImpl<ArticleTextSegMapper, ArticleTextSeg> implements IArticleTextSegService {

}
