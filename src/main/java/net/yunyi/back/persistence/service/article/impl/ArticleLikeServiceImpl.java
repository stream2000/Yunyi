package net.yunyi.back.persistence.service.article.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.ArticleLike;
import net.yunyi.back.persistence.mapper.ArticleLikeMapper;
import net.yunyi.back.persistence.service.article.IArticleLikeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-01
 */
@Service
public class ArticleLikeServiceImpl extends ServiceImpl<ArticleLikeMapper, ArticleLike> implements IArticleLikeService {

}
