package net.yunyi.back.persistence.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.vo.LoginVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author stream2000
 * @since 2020-10-07
 */
public interface IUserService extends IService<User> {

	ApiResult<LoginVo> registerAndLoginByCaptcha(String requestId, String captcha);

	ApiResult<Boolean> modifyUserInformation(User user, String nickName, String email, int age);
}
