package nian.shop.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import nian.shop.DTO.ResultDTO;
import nian.shop.entity.User;
import nian.shop.redis.UserKey;
import nian.shop.rocketmq.RocketMQReceiver;
import nian.shop.rocketmq.RocketMQSender;
import nian.shop.service.MqSender;
import nian.shop.service.RedisService;
import nian.shop.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;
	@Autowired
	MqSender mqSender;
	@Autowired
	RocketMQSender rocketMQSender;
	@Autowired
	RocketMQReceiver rocketMQReceiver;

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
	
	@RequestMapping("/mq")
	@ResponseBody
	ResultDTO<String> mq() {
		try {
			rocketMQSender.sendSecondKillMessage("hello, world");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultDTO.success("hello, world");
	}
	
	@RequestMapping("/mq1")
	@ResponseBody
	ResultDTO<String> mq1() {
		try {
			rocketMQSender.sendSecondKillMessage("hello, tianlei");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultDTO.success("hello, world");
	}
	
//	@RequestMapping("/mq")
//	@ResponseBody
//	ResultDTO<String> mq() {
//		mqSender.send("hello, world");
//		return ResultDTO.success("hello, world");
//	}
//	
//	@RequestMapping("/mq/topic")
//	@ResponseBody
//	ResultDTO<String> mq_topic() {
//		mqSender.sendTopic("hello, world");
//		return ResultDTO.success("hello, world");
//	}
//	
//	@RequestMapping("/mq/fanout")
//    @ResponseBody
//    public ResultDTO<String> fanout() {
//		mqSender.sendFanout("hello,world");
//        return ResultDTO.success("Hello，world");
//    }
//	@RequestMapping("/mq/header")
//    @ResponseBody
//    public ResultDTO<String> header() {
//		mqSender.sendHeader("hello,world");
//        return ResultDTO.success("Hello，world");
//    }
}
