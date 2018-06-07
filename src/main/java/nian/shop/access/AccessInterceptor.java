package nian.shop.access;

import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import nian.shop.DTO.ResultDTO;
import nian.shop.entity.SecondUser;
import nian.shop.redis.AccessKey;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;

@Service
public class AccessInterceptor  extends HandlerInterceptorAdapter{
	
	@Autowired
	SecondUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {
			SecondUser user = getUser(request, response);
			//保存用户信息
			UserContext.setUser(user);
			
			HandlerMethod hm = (HandlerMethod)handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if(needLogin) {
				if(user == null) {
					render(response, ResultDTO.fail("session错误"));
					return false;
				}
				key += "_" + user.getId();
			}else {
				//nothing to do 
			}
			AccessKey accessKey = AccessKey.withExpire(seconds);
			Integer count = redisService.get(accessKey, key, Integer.class);
	    	if(count  == null) {
	    		 redisService.set(accessKey, key, 1);
	    	} else if(count < maxCount) {
	    		 redisService.incr(accessKey, key);
	    	} else {
	    		render(response, ResultDTO.fail("请求过于频繁"));
	    		return false;
	    	}
		}
		return true;
	}
	
	private void render(HttpServletResponse response, ResultDTO<String> resultDTO)throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str  = JSON.toJSONString(resultDTO);
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	private SecondUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(SecondUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, SecondUserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
	}
	
	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[]  cookies = request.getCookies();
		if(cookies == null || cookies.length <= 0){
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
}
