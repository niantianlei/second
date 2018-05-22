package nian.shop.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.LoginVO;
import nian.shop.dao.SecondUserDao;
import nian.shop.entity.SecondUser;
import nian.shop.exception.SecondException;
import nian.shop.redis.SecondUserKey;
import nian.shop.utils.MD5Util;
import nian.shop.utils.ResultCode;
import nian.shop.utils.UUIDUtil;
import nian.shop.utils.ValidatorUtil;

/**
 * 
 * @author Niantianlei
 * @date 2018年5月19日 下午4:45:17
 */
@Service
public class SecondUserService {

	public static final String COOKIE_NAME_TOKEN = "token";
	
	@Autowired
	SecondUserDao secondUserDao;
	
	@Autowired
	RedisService redisService;
	
	public SecondUser getById(long id) {
		return secondUserDao.getById(id);
	}
	
	public SecondUser getByToken(HttpServletResponse response, String token) {
		if(ValidatorUtil.isEmpty(token)) {
			return null;
		}
		SecondUser user = redisService.get(SecondUserKey.token, token, SecondUser.class);
		//刷新有效时间
		if(user != null) addCookie(response, user);
		return user;
	}

	public boolean login(HttpServletResponse response ,LoginVO loginVO) {
		if(loginVO == null) {
			throw new SecondException(ResultDTO.error(ResultCode.SERVER_ERROR.getCode(), "服务端异常"));
		}
		String mobile = loginVO.getMobile();
		String formPassword = loginVO.getPassword();
		//判断手机号是否存在
		SecondUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new SecondException(ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "手机号码不存在"));
		}
		//验证密码
		String dbPassword = user.getPassword();
		String saltDB = user.getSalt();
		String afterProcessPassword = MD5Util.constructDBPassword(formPassword, saltDB);
		if(!afterProcessPassword.equals(dbPassword)) {
			throw new SecondException(ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "密码错误"));
		}
		
		addCookie(response, user);
		return true;
	}
	//生成cookie
	private void addCookie(HttpServletResponse response,SecondUser user) {
		String token = UUIDUtil.uuid();
		redisService.set(SecondUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(SecondUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
