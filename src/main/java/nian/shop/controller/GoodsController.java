package nian.shop.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nian.shop.entity.SecondUser;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;
import nian.shop.utils.ValidatorUtil;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	SecondUserService userService;
	
	@Autowired
	RedisService redisService;
	
/*    @RequestMapping("/to_list")
    public String list(HttpServletResponse response, Model model,
    		@CookieValue(value=SecondUserService.COOKIE_NAME_TOKEN, required=false)String cookieToken, 
    		@RequestParam(value=SecondUserService.COOKIE_NAME_TOKEN, required=false)String paramToken) {
        if(ValidatorUtil.isEmpty(cookieToken) == ValidatorUtil.isEmpty(paramToken)) {
        	return "login";
        }
        String token = ValidatorUtil.isEmpty(paramToken) ? cookieToken : paramToken;
        SecondUser user = userService.getByToken(response, token);
        model.addAttribute("user", user);
    	return "goods_list";
    }*/
    @RequestMapping("/to_list")
    public String list(Model model, SecondUser user) {
        model.addAttribute("user", user);
    	return "goods_list";
    }
    
}
