package nian.shop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nian.shop.VO.GoodsVo;
import nian.shop.dao.OrderDao;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.utils.SecondStatusEnum;

@Service
public class OrderService {
	@Autowired
	OrderDao orderDao;
	
	public SecondOrder getSecondOrderByUserIdandGoodsId(long userId, long goodsId) {
		return orderDao.getSecondOrderByUserIdandGoodsId(userId, goodsId);
	}

	@Transactional
	public OrderInfo createOrder(SecondUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getSecondPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(SecondStatusEnum.NO_PAY.getStatus());
		orderInfo.setUserId(user.getId());
		long orderId = orderDao.insert(orderInfo);
		SecondOrder secondOrder = new SecondOrder();
		secondOrder.setGoodsId(goods.getId());
		secondOrder.setOrderId(orderId);
		secondOrder.setUserId(user.getId());
		orderDao.insertSecondOrder(secondOrder);
		return orderInfo;
	}

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}
	

}
