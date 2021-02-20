package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.istack.internal.NotNull;
import java.util.UUID;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.auth.JWTUtils;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.mapper.UserMapper;
import net.yunyi.back.persistence.service.ISmsService;
import net.yunyi.back.persistence.service.IUserService;
import net.yunyi.back.persistence.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ISmsService smsService;

    @Override
    public ApiResult<LoginVo> registerAndLoginByCaptcha(@NotNull String requestId,
        @NotNull String captcha) {
        String phone = smsService.checkCaptcha(requestId, captcha);
        if (phone == null) {
            throw new BizException(YunyiCommonEnum.AUTH.getResultCode(), "wrong captcha code");
        }
        LoginVo vo = new LoginVo();
        User user = getOne(new QueryWrapper<User>().eq("phone", requestId));
        if (user == null) {
            user = new User();
            user.setAge(0);
            user.setPhone(phone);
            String id = UUID.randomUUID().toString();
            user.setName("user-" + id);
            baseMapper.insert(user);
            vo.setNewUser(true);
        }
        String token = JWTUtils.getToken(user.getId());
        vo.setJwt(token);
        vo.setUser(user);
        return ApiResult.ok(vo);
    }
}
