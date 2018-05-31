package nian.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nian.rabbitmq.MQConfig;
import com.nian.rabbitmq.SecondKillMessage;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;

/**
* @author created by NianTianlei
* @createDate on 2018年5月29日 下午1:08:52
*/
@Service
public class MqReceiver {
	private static Logger log = LoggerFactory.getLogger(MqReceiver.class);
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SecondKillService secondKillService;
	
	@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
	public void receive(String message) {
		log.info("receive message: " + message);
		SecondKillMessage sm  = RedisService.stringToBean(message, SecondKillMessage.class);
		SecondUser user = sm.getUser();
		long goodsId = sm.getGoodsId();
		
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		return;
    	}
    	
    	//判断是否已经秒杀到了
    	SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		return;
    	}
    	//减库存 下订单 写入秒杀订单
    	secondKillService.secondKill(user, goods);
	}
	
//	@RabbitListener(queues=MQConfig.QUEUE)
//	public void receive(String msg) {
//		log.info("receive message: " + msg);
//	}
//	@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
//	public void receiveTopic1(String message) {
//		log.info(" topic queue1 message:" + message);
//	}
//	
//	@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
//	public void receiveTopic2(String message) {
//		log.info(" topic queue2 message:" + message);
//	}
//	@RabbitListener(queues=MQConfig.HEADER_QUEUE)
//	public void receiveHeaderQueue(byte[] message) {
//		log.info(" header  queue message:" + new String(message));
//	}
}
