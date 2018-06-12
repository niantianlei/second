package nian.shop.rocketmq;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.nian.rabbitmq.MQConfig;
import com.nian.rabbitmq.SecondKillMessage;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.service.GoodsService;
import nian.shop.service.MqReceiver;
import nian.shop.service.OrderService;
import nian.shop.service.RedisService;
import nian.shop.service.SecondKillService;

/**
* @author created by NianTianlei
* @createDate on 2018年6月11日 上午8:06:26
*/
@Configuration
public class RocketMQReceiver {
	private static Logger log = LoggerFactory.getLogger(MqReceiver.class);

	@Autowired
	GoodsService goodsService;
	@Autowired
	OrderService orderService;
	@Autowired
	SecondKillService secondKillService;
	
/*	public void receive() {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConfig.SECOND_GROUP);
        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(RocketMQConfig.namesrvAddr);

        try {
            consumer.subscribe(RocketMQConfig.SECOND_TOPIC, "push");
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            
            
            
            
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    System.out.println(JSON.toJSONString(msgs));
                	for(MessageExt messageExt : msgs){
                        byte[] bytes = messageExt.getBody();
                        String string = new String(bytes);
                        log.info("receive message: " + string);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        } catch (MQClientException e) {
            e.printStackTrace();
        }
	}
	*/
	@PostConstruct
    public void miaoShaConsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConfig.SECOND_GROUP);
        //可以设置多个地址，以;隔开
        consumer.setNamesrvAddr(RocketMQConfig.namesrvAddr);
        try {
            consumer.subscribe(RocketMQConfig.SECOND_TOPIC, "push");
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                	for(MessageExt messageExt : msgs){
                        byte[] bytes = messageExt.getBody();
                        String string = new String(bytes);
                        log.info("receive message: " + string);
                        //至此我们已经获取到了秒杀用户的信息
                        SecondKillMessage secondKillMessage = RedisService.stringToBean(string, SecondKillMessage.class);
                        SecondUser secondUser = secondKillMessage.getUser();
                        long goodsId = secondKillMessage.getGoodsId();
                        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
                        int stock = goods.getStockCount();
                        if(stock <= 0) {
                           break;
                        }

                        //根据用户和id查看当前用户是否秒杀到
                        SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(secondUser.getId(), goodsId);
                        if(order != null) {
                            continue;
                        }
                        //减库存 下订单 写入秒杀订单
                        secondKillService.secondKill(secondUser, goods);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
