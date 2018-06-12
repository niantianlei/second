package nian.shop.rocketmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

/**
* @author created by NianTianlei
* @createDate on 2018年6月11日 上午7:50:01
*/
@Configuration
public class RocketMQConfig {
	
	public static final String SECOND_TOPIC = "secondKilling_topic";

    public static final String SECOND_GROUP = "secondKilling_group";

    public static final String namesrvAddr = "127.0.0.1:9876";
    
    @Bean
    public DefaultMQProducer defaultMQProducer(){
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(RocketMQConfig.SECOND_GROUP);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(0);
        try {
            defaultMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return defaultMQProducer;
    }
}
