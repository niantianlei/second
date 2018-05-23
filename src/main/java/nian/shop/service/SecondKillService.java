package nian.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondUser;

@Service
public class SecondKillService {
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Transactional
	public OrderInfo secondKill(SecondUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		goodsService.reduceStock(goods);
		return orderService.createOrder(user, goods);
	}
}
