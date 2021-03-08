package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.TransComment;
import net.yunyi.back.persistence.mapper.TransCommentMapper;
import net.yunyi.back.persistence.service.ITransCommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 翻译评论表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class TransCommentServiceImpl extends ServiceImpl<TransCommentMapper, TransComment> implements ITransCommentService {

}
