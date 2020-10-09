package net.yunyi.back.controller;

import io.swagger.annotations.ApiOperation;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.persistence.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    ISmsService smsService;

    @GetMapping
    @ResponseBody
    @ApiOperation("发送验证码并返回request id")
    public ApiResult<String> getCaptcha(@RequestParam String phone) {
        return ApiResult.ok(smsService.sendCaptcha(phone));
    }

}
