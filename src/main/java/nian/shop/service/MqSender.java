package nian.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nian.rabbitmq.MQConfig;
import com.nian.rabbitmq.SecondKillMessage;

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
//	public void send(Object message) {
//		String msg = RedisService.beanToString(message);
//		log.info("send message: " + message);
//		amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//	}
//	public void sendTopic(Object message) {
//		String msg = RedisService.beanToString(message);
//		log.info("send topic message:" + msg);
//		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg + "1");
//		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg + "2");
//	}
//	public void sendFanout(Object message) {
//		String msg = RedisService.beanToString(message);
//		log.info("send fanout message:" + msg);
//		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
//	}
//	public void sendHeader(Object message) {
//		String msg = RedisService.beanToString(message);
//		log.info("send fanout message:" + msg);
//		MessageProperties properties = new MessageProperties();
//		properties.setHeader("header1", "value1");
//		properties.setHeader("header2", "value2");
//		Message obj = new Message(msg.getBytes(), properties);
//		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//	}
	
	public void sendSecondKillMessage(SecondKillMessage sm) {
		String msg = RedisService.beanToString(sm);
		log.info("send message:" + msg);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}
}
