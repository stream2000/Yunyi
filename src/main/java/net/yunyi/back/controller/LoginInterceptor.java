package net.yunyi.back.controller;

import net.yunyi.back.common.BizException;
import net.yunyi.back.common.LoginEnable;
import net.yunyi.back.common.LoginRequired;
import net.yunyi.back.common.auth.JWTUtils;
import net.yunyi.back.common.response.YunyiCommonEnum;
import net.yunyi.back.persistence.entity.User;
import net.yunyi.back.persistence.service.common.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	IUserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// cors
		response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		// ①:START 方法注解级拦截器
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		// 判断接口是否需要登录
		boolean mustLogin = method.getAnnotation(LoginRequired.class) != null;
		boolean enableLogin = method.getAnnotation(LoginEnable.class) != null;

		// 有 @LoginRequired 注解，需要认证
		if (mustLogin || enableLogin) {
			// 这写你拦截需要干的事儿，比如取缓存，SESSION，权限判断等
			String token = request.getParameter("token");
			if (mustLogin && !StringUtils.hasText(token)) {
				throw new BizException(YunyiCommonEnum.AUTH);
			}
			long uid = JWTUtils.getInstance().checkToken(token);
			User user = userService.getById(uid);
			if (user == null) {
				throw new BizException(YunyiCommonEnum.AUTH_USER_NOT_FOUND);
			}
			request.setAttribute("user", user);
			return true;
		}
		return true;
	}
}