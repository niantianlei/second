package nian.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.LoginVO;
import nian.shop.dao.SecondUserDao;
import nian.shop.entity.SecondUser;
import nian.shop.utils.MD5Util;
import nian.shop.utils.ResultCode;

/**
 * 
 * @author Niantianlei
 * @date 2018年5月19日 下午4:45:17
 */
@Service
public class SecondUserService {

	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	SecondUserDao secondUserDao;
	
	@Autowired
	RedisService redisService;
	
	public SecondUser getById(long id) {
		return secondUserDao.getById(id);
	}

	public ResultDTO<String> login(LoginVO loginVO) {
		if(loginVO == null) {
			return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "请求为空");
		}
		String mobile = loginVO.getMobile();
		String formPassword = loginVO.getPassword();
		//判断手机号是否存在
		SecondUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "手机号不存在");
		}
		//验证密码
		String dbPassword = user.getPassword();
		String saltDB = user.getSalt();
		String afterProcessPassword = MD5Util.constructDBPassword(formPassword, saltDB);
		if(!afterProcessPassword.equals(dbPassword)) {
			return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "密码错误");
		}
		return ResultDTO.success("success");
	}
	

	/*public MiaoshaUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
	

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token	 = UUIDUtil.uuid();
		addCookie(response, token, user);
		return true;
	}
	
	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
		redisService.set(MiaoshaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}*/
}
