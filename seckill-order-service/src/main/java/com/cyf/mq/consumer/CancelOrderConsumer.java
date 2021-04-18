package com.cyf.mq.consumer;

import com.cyf.msg.CancelOrderMessage;
import com.cyf.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 取消订单消费者
 *
 * @author 陈一锋
 * @date 2021/4/11 17:53
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = CancelOrderMessage.TOPIC,
        consumerGroup = "${spring.application.name}-consumer-group")
public class CancelOrderConsumer implements RocketMQListener<CancelOrderMessage> {

    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(CancelOrderMessage message) {
        log.info("接收到取消订单消息,订单编号:{},当前时间:{}", message.getOrderSn(), LocalDateTime.now());
        orderService.cancelOrder(message.getOrderSn());
    }
}
