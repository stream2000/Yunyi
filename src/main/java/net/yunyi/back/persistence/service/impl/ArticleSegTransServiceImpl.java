package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.ArticleSegTrans;
import net.yunyi.back.persistence.mapper.ArticleSegTransMapper;
import net.yunyi.back.persistence.service.IArticleSegTransService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 原文切分的翻译表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-08
 */
@Service
public class ArticleSegTransServiceImpl extends ServiceImpl<ArticleSegTransMapper, ArticleSegTrans> implements IArticleSegTransService {

}
