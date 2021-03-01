package net.yunyi.back.controller;

import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.persistence.service.IUserService;
import net.yunyi.back.persistence.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/login")
public class LoginController {

    @Autowired
    IUserService userService;

    @GetMapping("/captcha")
    @ResponseBody
    @ApiOperation(value = "验证码登录/注册接口", notes = "request id为取验证码接口的返回值")
    public ApiResult<LoginVo> registerThenLoginByCaptcha(@RequestParam String requestId, @RequestParam String captcha) {

        return userService.registerAndLoginByCaptcha(requestId, captcha);
    }

    @GetMapping("/password")
    @ResponseBody
    @ApiOperation(value = "密码登录接口")
    public ApiResult<LoginVo> LoginByPassword(@RequestParam @NotBlank String requestId, @RequestParam @NotBlank @Digits(integer = 4, fraction = 0) String captcha) {
        return userService.registerAndLoginByCaptcha(requestId, captcha);
    }

}
