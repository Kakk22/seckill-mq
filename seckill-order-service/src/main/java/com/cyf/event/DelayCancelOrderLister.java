package com.cyf.event;


import com.cyf.enums.MessagesProtocolEnum;
import com.cyf.mq.MQProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 观察者模式
 * <p>
 * 延时取消订单监听器
 * <p>
 * 创建订单成功后
 * 发送延时取消消息
 *
 * @author 陈一锋
 * @date 2021/4/10 22:32
 **/
@Slf4j
@Component
public class DelayCancelOrderLister implements ApplicationListener<CreateOrderEvent> {

    @Autowired
    private MQProducer mqProducer;

    @Override
    public void onApplicationEvent(CreateOrderEvent event) {
        String orderSn = event.getOrderSn();

        Message message = new Message(MessagesProtocolEnum.CANCEL_ORDER_TOPIC.getTopic(), orderSn.getBytes());
        //private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        //试一下10秒后取消
        message.setDelayTimeLevel(3);

        DefaultMQProducer producer = mqProducer.getProducer();
        try {
            producer.send(message);
            log.info("取消订单监听器发送取消订单mq消息成功,消息内容:{}", message.toString());
        } catch (MQClientException | RemotingException | InterruptedException | MQBrokerException e) {
            log.error("发送取消订单消息失败,订单编号:{},异常消息:{}", orderSn, e.getMessage());
        }

    }
}
