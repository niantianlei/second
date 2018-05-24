package nian.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nian.shop.DTO.ResultDTO;
import nian.shop.entity.SecondUser;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;




@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	SecondUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/info")
    @ResponseBody
    public ResultDTO<SecondUser> info(Model model, SecondUser user) {
        return ResultDTO.success(user);
    }
    
}
