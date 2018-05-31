package nian.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.redis.SecondKey;

@Service
public class SecondKillService {
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo secondKill(SecondUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		boolean res = goodsService.reduceStock(goods);
		if(res) {
			return orderService.createOrder(user, goods);
		} else {
			setGoodsOver(goods.getId());
			return null;
		}
	}


	public long getSecondKillResult(Long userId, long goodsId) {
		SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(userId, goodsId);
		if(order != null) {
			return order.getOrderId();
		} else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	private void setGoodsOver(Long goodsId) {
		redisService.set(SecondKey.isGoodsOver, "" + goodsId, true);
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.isExist(SecondKey.isGoodsOver, "" + goodsId);
	}


	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}
}
