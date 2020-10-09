package net.yunyi.back.controller;


import io.swagger.annotations.ApiOperation;
import java.util.List;
import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.response.ApiResult;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author stream2000
 * @since 2020-10-07
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("exception")
    @ResponseBody
    public ApiResult<List<User>> getUsersExcept() {
        throw new BizException(YunyiCommonEnum.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("list")
    @ResponseBody
    @LoginRequired
    @ApiOperation("获取用户列表，需要登录，用于验证登录功能")
    public ApiResult<List<User>> getUserList() {
        return ApiResult.ok(userService.list());
    }

    @GetMapping("id")
    @ResponseBody
    @ApiOperation("获取单个用户，不需要登录")
    public ApiResult<User> getUserById(@RequestParam int id) {
        return ApiResult.ok(userService.getById(id));
    }

}

