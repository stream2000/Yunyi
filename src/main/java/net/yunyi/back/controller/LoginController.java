package net.yunyi.back.controller;

import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.persistence.service.IUserService;
import net.yunyi.back.persistence.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    IUserService userService;

    // TODO add some validation logic
    @GetMapping("/captcha")
    @ResponseBody
    public ApiResult<LoginVo> registerThenLoginByCaptcha(@RequestParam String phone,
        @RequestParam String captcha) {
        return userService.registerAndLoginByCaptcha(phone, captcha);
    }

}
