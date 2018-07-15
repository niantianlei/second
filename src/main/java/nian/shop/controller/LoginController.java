package nian.shop.controller;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import nian.shop.DTO.ResultDTO;
import nian.shop.VO.LoginVO;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;

@Controller
@RequestMapping("/login")
public class LoginController {
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
    public ResultDTO<String> doLogin(HttpServletResponse response, @Valid LoginVO loginVO) {
    	String token = secondUserService.login(response, loginVO);
    	return ResultDTO.success(token);
    }
}
