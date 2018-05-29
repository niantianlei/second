package nian.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nian.rabbitmq.MQConfig;

/**
* @author created by NianTianlei
* @createDate on 2018年5月29日 上午10:05:43
*/
@Service
public class MqSender {
	private static Logger log = LoggerFactory.getLogger(MqSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	/*public void sendMiaoshaMessage(MiaoshaMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}*/
	public void send(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send message: " + message);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}
	public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send topic message:" + msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg + "1");
		log.info("send topic.key1 finish");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg + "2");
		log.info("send topic.key2 finish");
	}
}
