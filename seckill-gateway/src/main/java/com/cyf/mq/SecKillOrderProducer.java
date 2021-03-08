package com.cyf.mq;

import com.cyf.common.RocketMqConfig;
import com.cyf.enums.MessagesProtocolEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 秒杀订单生产者 初始化
 *
 * @author 陈一锋
 * @date 2021/3/8 21:57
 **/
@Slf4j
@Component
public class SecKillOrderProducer {

    @Resource
    private RocketMqConfig rocketMqConfig;

    private DefaultMQProducer defaultMQProducer;

    @PostConstruct
    void init() {

        defaultMQProducer = new DefaultMQProducer(MessagesProtocolEnum.SECKILL_ORDER_TOPIC.getProducerGroup());
        //设置mq地址
        defaultMQProducer.setNamesrvAddr(rocketMqConfig.getNameServerAddress());
        //设置失败重试次数
        defaultMQProducer.setRetryTimesWhenSendFailed(3);

        try {
            defaultMQProducer.start();
        } catch (MQClientException e) {
            log.error("MQ producer start error:{}",e.getMessage());
            throw new RuntimeException("秒杀订单生产者SecKillOrderProducer加载异常");
        }
        log.info("秒杀订单生产者SecKillOrderProducer启动成功!");
    }

    public DefaultMQProducer getProducer() {
        return defaultMQProducer;
    }
}
