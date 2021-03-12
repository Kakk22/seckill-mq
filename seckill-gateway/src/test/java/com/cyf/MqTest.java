package com.cyf;

import com.cyf.enums.MessagesProtocolEnum;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

/**
 * @author 陈一锋
 * @date 2021/3/12 15:15
 **/
public class MqTest {


    @Test
    public void t1() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(MessagesProtocolEnum.SECKILL_ORDER_TOPIC.getProducerGroup());
        //设置mq地址
        defaultMQProducer.setNamesrvAddr("47.107.53.172:9876");
        //设置失败重试次数
        defaultMQProducer.setRetryTimesWhenSendFailed(3);
        defaultMQProducer.start();

        byte[] bytes = new byte[10];
        bytes[0] = 0;
        try {
            defaultMQProducer.send(new Message("topic", bytes));
            System.out.println("mq send success");
        } catch (MQClientException | InterruptedException | MQBrokerException | RemotingException e) {
            e.printStackTrace();
        }
    }
}
