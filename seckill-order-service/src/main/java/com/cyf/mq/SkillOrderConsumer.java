package com.cyf.mq;

import com.cyf.config.RocketMqConfig;
import com.cyf.enums.MessagesProtocolEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 秒杀订单消息消费者
 *
 * @author 陈一锋
 * @date 2021/3/18 22:05
 **/
@Component
@Slf4j
public class SkillOrderConsumer {

    @Resource
    private RocketMqConfig rocketMqConfig;

    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Resource
    private MessageListenerConcurrently seckillOrderLister;


    @PostConstruct
    public void init() {
        defaultMQPushConsumer = new DefaultMQPushConsumer(MessagesProtocolEnum.SECKILL_ORDER_TOPIC.getConsumerGroup());
        //设置nameServer地址
        defaultMQPushConsumer.setNamesrvAddr(rocketMqConfig.getNameServerAddress());
        //从头开始消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //消费模式
        //1.集群模式 同一条消息只被一个消费者消费
        //2.广播模式 同一条消息可被多个消费者消费
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        //注册监听器
        defaultMQPushConsumer.registerMessageListener(seckillOrderLister);
        try {
            //订阅主题
            defaultMQPushConsumer.subscribe(MessagesProtocolEnum.SECKILL_ORDER_TOPIC.getTopic(), "*");
            //启动消费者
            defaultMQPushConsumer.start();
            log.info("秒杀订单消费者启动成功");
        } catch (MQClientException e) {
            log.error("秒杀订单消费者启动失败,失败原因:{}", e.getMessage());
            throw new RuntimeException("SkillOrderConsumer秒杀订单消费者启动失败");
        }

    }
}
