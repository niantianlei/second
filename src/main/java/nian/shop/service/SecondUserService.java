package nian.shop.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.LoginVO;
import nian.shop.bloomFilter.BloomFilterCache;
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
	private static Logger log = LoggerFactory.getLogger(SecondUserService.class);
	public static final String COOKIE_NAME_TOKEN = "token";
	
	@Autowired
	SecondUserDao secondUserDao;
	
	@Autowired
	RedisService redisService;
	
	public SecondUser getById(long id) {
		//缓存优化
		/*SecondUser user = redisService.get(SecondUserKey.getById, "" + id, SecondUser.class);
		if(user != null) {
			return user;
		}
		user = secondUserDao.getById(id);
		if(user != null) {
			redisService.set(SecondUserKey.getById, "" + id, SecondUser.class);
		}*/
		SecondUser user = secondUserDao.getById(id);
		return user;
	}
	
	public boolean updatePassword(String token, long id, String newPassword) {
		SecondUser user = getById(id);
		if(user == null) {
			throw new SecondException(ResultDTO.error("user不存在"));
		}
		//更新数据库
		SecondUser toBeUpdate = new SecondUser();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.constructDBPassword(newPassword, user.getSalt()));
		secondUserDao.update(toBeUpdate);
		/**
		 * 处理缓存
		 */
		//删user
		redisService.delete(SecondUserKey.getById, "" + id);
		//更新token
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(SecondUserKey.token, token, user);
		return true;
	}
	
	public SecondUser getByToken(HttpServletResponse response, String token) {
		if(ValidatorUtil.isEmpty(token)) {
			return null;
		}
		SecondUser user = redisService.get(SecondUserKey.token, token, SecondUser.class);
		//刷新有效时间
		if(user != null) addCookie(response, token, user);
		return user;
	}

	public String login(HttpServletResponse response, LoginVO loginVO) {
		log.info("登陸信息: " + JSON.toJSONString(loginVO));
		if(loginVO == null) {
			throw new SecondException(ResultDTO.error(ResultCode.SERVER_ERROR.getCode(), "服务端异常"));
		}
		
		//首先要过布隆过滤器
		if(!BloomFilterCache.bloomFilter.check(loginVO.getMobile())) {
			throw new SecondException(ResultDTO.fail("该用户不存在（未通过布隆过滤器）"));
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
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return token;
	}
	//生成cookie
	private void addCookie(HttpServletResponse response, String token, SecondUser user) {
		redisService.set(SecondUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(SecondUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
