package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.TransItemComment;
import net.yunyi.back.persistence.mapper.TransItemCommentMapper;
import net.yunyi.back.persistence.service.ITransItemCommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单句评论表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class TransItemCommentServiceImpl extends ServiceImpl<TransItemCommentMapper, TransItemComment> implements ITransItemCommentService {

}
