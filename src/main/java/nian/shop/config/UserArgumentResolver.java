package nian.shop.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nian.shop.entity.SecondUser;
import nian.shop.service.SecondUserService;
import nian.shop.utils.ValidatorUtil;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	SecondUserService userService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz == SecondUser.class;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		String paramToken = request.getParameter(SecondUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, SecondUserService.COOKIE_NAME_TOKEN);
		if(ValidatorUtil.isEmpty(cookieToken) && ValidatorUtil.isEmpty(paramToken)) {
			return null;
		}
		String token = ValidatorUtil.isEmpty(paramToken) ? cookieToken : paramToken;
		return userService.getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[]  cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
