package nian.shop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nian.shop.DTO.ResultDTO;
import nian.shop.service.RedisService;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public ResultDTO<Boolean> doLogin() {
    	/*log.info(loginVo.toString());
    	//登录
    	userService.login(response, loginVo);
    	return Result.success(true);*/
    	return null;
    }
}
