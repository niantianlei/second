package nian.shop.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.LoginVO;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;
import nian.shop.utils.ResultCode;
import nian.shop.utils.ValidatorUtil;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	RedisService redisService;
	@Autowired
	SecondUserService secondUserService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    
    @RequestMapping("/do_login")
    @ResponseBody
    public ResultDTO<String> doLogin(LoginVO loginVO) {
    	log.info(loginVO.toString());
    	//参数校验
    	String passInput = loginVO.getPassword();
    	String mobile = loginVO.getMobile();
    	if(StringUtils.isEmpty(passInput)) {
    		return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "密码为空");
    	}
    	if(StringUtils.isEmpty(mobile)) {
    		return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "手机号为空");
    	}
    	if(!ValidatorUtil.isMobile(mobile)) {
    		return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), "手机号格式错误");
    	}
    	//登录
    	ResultDTO<String> res = secondUserService.login(loginVO);
    	return res;
    }
}
