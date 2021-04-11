package com.cyf.event;


import com.cyf.enums.MessagesProtocolEnum;
import com.cyf.msg.CancelOrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.support.MessageBuilder;
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

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onApplicationEvent(CreateOrderEvent event) {

        String orderSn = event.getOrderSn();
        //private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        //试一下10秒后取消
        // message.setDelayTimeLevel(3);

        try {
            //构建消息
            CancelOrderMessage cancelOrderMessage = new CancelOrderMessage();
            cancelOrderMessage.setOrderSn(orderSn);

            SendResult sendResult = rocketMQTemplate.syncSend(CancelOrderMessage.TOPIC,
                    MessageBuilder.withPayload(cancelOrderMessage).build(), 30000, 3);
            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("取消订单监听器发送取消订单mq消息成功,订单编号:{}", orderSn);
            } else {
                log.error("取消订单监听器发送取消订单mq消息失败,订单编号:{},失败结果:{}", orderSn, sendResult.toString());
            }

        } catch (Exception e) {
            log.error("取消订单监听器发送取消订单mq消息异常,异常信息:{}", e.getMessage());
        }

    }
}