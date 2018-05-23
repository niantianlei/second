package nian.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.service.GoodsService;
import nian.shop.service.OrderService;
import nian.shop.service.RedisService;
import nian.shop.service.SecondKillService;
import nian.shop.service.SecondUserService;
import nian.shop.utils.SecondResEnum;

@Controller
@RequestMapping("/second")
public class SecondController {

	@Autowired
	SecondUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SecondKillService secondKillService;
	
    @RequestMapping("/do_secondKill")
    public String list(Model model, SecondUser user,
    		@RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return "login";
    	}
    	//判断库存
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		model.addAttribute("errmsg", SecondResEnum.SECOND_KILL_OVER.getMsg());
    		return "secondKill_fail";
    	}
    	//判断是否已经秒杀到了
    	SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		model.addAttribute("errmsg", SecondResEnum.SECOND_KILL_REPEAT.getMsg());
    		return "secondKill_fail";
    	}
    	//减库存 下订单 写入秒杀订单
    	OrderInfo orderInfo = secondKillService.secondKill(user, goods);
    	model.addAttribute("orderInfo", orderInfo);
    	model.addAttribute("goods", goods);
        return "order_detail";
    }
}
