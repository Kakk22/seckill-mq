package com.cyf.enums;

import lombok.Getter;

/**
 * 消息协议枚举
 *
 * @author 陈一锋
 * @date 2021/3/8 22:21
 **/
@Getter
public enum MessagesProtocolEnum {
    /**
     * 秒杀订单消息协议
     */
    SECKILL_ORDER_TOPIC("SECKILL_ORDER_TOPIC", "ORDER_PRODUCER_GROUP", "ORDER_CONSUMER_GROUP", "秒杀订单协议"),

    /**
     * 延时取消订单
     */
    CANCEL_ORDER_TOPIC("CANCEL_ORDER_TOPIC", "ORDER_PRODUCER_GROUP", "ORDER_CONSUMER_GROUP", "延时取消协议");

    /**
     * 主题
     */
    private final String topic;
    /**
     * 生产者组
     */
    private final String producerGroup;
    /**
     * 消费者组
     */
    private final String consumerGroup;
    /**
     * 描述
     */
    private final String desc;

    MessagesProtocolEnum(String topic, String producerGroup, String consumerGroup, String desc) {
        this.topic = topic;
        this.producerGroup = producerGroup;
        this.consumerGroup = consumerGroup;
        this.desc = desc;
    }
}
