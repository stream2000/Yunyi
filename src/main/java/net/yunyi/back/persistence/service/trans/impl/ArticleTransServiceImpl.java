package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.ArticleTrans;
import net.yunyi.back.persistence.mapper.ArticleTransMapper;
import net.yunyi.back.persistence.service.trans.IArticleTransService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 原文切分的翻译表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class ArticleTransServiceImpl extends ServiceImpl<ArticleTransMapper, ArticleTrans> implements IArticleTransService {

}
