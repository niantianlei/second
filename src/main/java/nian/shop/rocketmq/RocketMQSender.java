package nian.shop.rocketmq;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import nian.shop.controller.SecondController;
import nian.shop.service.RedisService;

/**
* @author created by NianTianlei
* @createDate on 2018年6月11日 上午7:57:54
*/
@Service
public class RocketMQSender {
	private static Logger log = LoggerFactory.getLogger(SecondController.class);

    @Autowired
    DefaultMQProducer defaultMQProducer;

    public void  sendSecondKillMessage(Object value) throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException {
        byte[] bytes = RedisService.beanToString(value).getBytes(RemotingHelper.DEFAULT_CHARSET);
        log.info("发送的消息是: " + new String(bytes));
        Message message = new Message(RocketMQConfig.SECOND_TOPIC, "push", bytes);
        defaultMQProducer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("成功了》》" + "消息码为: " + sendResult.getMsgId());
            }
            @Override
            public void onException(Throwable e) {
            	log.info("异常" + e);
                e.printStackTrace();
            }
        });
    }
}
