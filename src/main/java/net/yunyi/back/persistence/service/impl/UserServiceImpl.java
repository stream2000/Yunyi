package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.UUID;
import net.yunyi.back.common.auth.JWTUtils;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.mapper.UserMapper;
import net.yunyi.back.persistence.service.IUserService;
import net.yunyi.back.persistence.vo.LoginVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2020-10-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public ApiResult<LoginVo> registerAndLoginByCaptcha(String phone, String captcha) {
        // TODO check captcha
        User user = getOne(new QueryWrapper<User>().eq("phone", phone));
        if (user == null) {
            user = new User();
            user.setAge(0);
            user.setPhone(phone);
            String id = UUID.randomUUID().toString();
            user.setName("user-" + id);
            baseMapper.insert(user);
        }
        LoginVo vo = new LoginVo();
        Long id = user.getId();
        String token = JWTUtils.getInstance().getToken(user.getId());
        vo.setJwt(token);
        vo.setUser(user);
        return ApiResult.ok(vo);
    }
}
