package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.RequestTrans;
import net.yunyi.back.persistence.mapper.RequestTransMapper;
import net.yunyi.back.persistence.service.IRequestTransService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-02
 */
@Service
public class RequestTransServiceImpl extends ServiceImpl<RequestTransMapper, RequestTrans> implements IRequestTransService {

}
