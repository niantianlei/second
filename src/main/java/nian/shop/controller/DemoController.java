package nian.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nian.shop.DTO.ResultDTO;
import nian.shop.entity.User;
import nian.shop.redis.UserKey;
import nian.shop.service.RedisService;
import nian.shop.service.UserService;
/**
 * 
 * @author Niantianlei
 * @date 2018年5月17日 下午1:16:03
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

	@RequestMapping("/success")
	@ResponseBody
	ResultDTO<String> hello() {
		return ResultDTO.success("asd");
	}

	@RequestMapping("/fail")
	@ResponseBody
	ResultDTO<String> fail() {
		return ResultDTO.error("服务端异常");
	}

	@RequestMapping("/thymeleaf")
	String thymeleaf(Model model) {
		model.addAttribute("name", "tyler");
		return "hello";
	}

	@RequestMapping("/db/getById")
	@ResponseBody
	ResultDTO<User> getByIds() {
		User user = userService.getUserById(1);
		return ResultDTO.success(user);
	}

	@RequestMapping("/db/tx")
	@ResponseBody
	ResultDTO<Boolean> tx() {
		userService.tx();
		return ResultDTO.success(true);
	}

	@RequestMapping("/redis/get")
	@ResponseBody
	ResultDTO<User> redisGet() {
		User user = redisService.get(UserKey.getById, "" + 1, User.class);
		return ResultDTO.success(user);
	}

	@RequestMapping("/redis/set")
	@ResponseBody
	ResultDTO<Boolean> redisSet() {
		User user = new User();
		user.setId(1);
		user.setName("123456");
		redisService.set(UserKey.getById, "" + 1, user);
		return ResultDTO.success(true);
	}
}
