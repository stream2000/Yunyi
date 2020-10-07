package net.yunyi.back.persistence.service.impl;

import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.mapper.UserMapper;
import net.yunyi.back.persistence.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2020-10-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
