package nian.shop.rocketmq;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;

/**
* @author created by NianTianlei
* @createDate on 2018年6月10日 下午8:08:31
*/
public class ProducerDemo {
    public static void main(String[] args) throws Exception {
    	//Instantiate with a producer group name. 
        DefaultMQProducer producer = new DefaultMQProducer("second_group");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setInstanceName("producer");
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        System.out.println("开始发送数据");
        try {
        	//Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicD",// topic
            		"TagD",
                    "Hello Hello world Hello world Hello world Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));// body
            
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", "成功",
                        sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", "异常", e);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("全部发送完成");
        producer.shutdown();
    }
}