package nian.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.GoodsVo;
import nian.shop.VO.OrderDetailVo;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondUser;
import nian.shop.service.GoodsService;
import nian.shop.service.OrderService;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	SecondUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public ResultDTO<OrderDetailVo> info(Model model, SecondUser user,
    		@RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return ResultDTO.fail("session错误");
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return ResultDTO.fail("order不存在");
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return ResultDTO.success(vo);
    }
    
}
