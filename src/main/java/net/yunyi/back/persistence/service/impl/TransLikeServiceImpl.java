package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.TransLike;
import net.yunyi.back.persistence.mapper.TransLikeMapper;
import net.yunyi.back.persistence.service.ITransLikeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 翻译点赞表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class TransLikeServiceImpl extends ServiceImpl<TransLikeMapper, TransLike> implements ITransLikeService {

}
