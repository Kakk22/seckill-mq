package com.cyf.mq.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyf.domain.Order;
import com.cyf.msg.OrderMsgProtocol;
import com.cyf.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.*;

/**
 * 秒杀订单监听器
 * 实际逻辑处理者
 *
 * @author 陈一锋
 * @date 2021/3/18 22:23
 **/
@Component
@Slf4j
public class SeckillOrderLister implements MessageListenerConcurrently {

    @Resource
    private OrderService orderService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        for (MessageExt ext : msgs) {
            String msgId = ext.getMsgId();
            int times = ext.getReconsumeTimes();
            String message = new String(ext.getBody());
            log.info("秒杀订单监听器接收到消息,消息id:{},消息时间:{},消息内容:{}", msgId, times, message);

            //消息解码
            OrderMsgProtocol orderMsgProtocol = new OrderMsgProtocol();
            orderMsgProtocol.decode(message);
            log.info("秒杀订单监听器解码消息,消息内容:{}", orderMsgProtocol.toString());

            //消息幂等:判断是否消费过
            String orderSn = orderMsgProtocol.getOrderSn();
            List<Order> list = orderService.list(new QueryWrapper<Order>().eq("order_sn", orderSn));
            if (CollectionUtil.isNotEmpty(list)) {
                log.warn("秒杀订单监听器:订单编号:{} 该订单已经消费过");
                return CONSUME_SUCCESS;
            }

            //业务幂等:判断同一个商品id +userPhone只有一个订单
            String productId = orderMsgProtocol.getProductId();
            String userPhone = orderMsgProtocol.getUserPhone();
            List<Order> businessList = orderService.list(new QueryWrapper<Order>()
                    .lambda()
                    .eq(Order::getProId, productId)
                    .eq(Order::getUserPhone, userPhone));
            if (CollectionUtil.isNotEmpty(businessList)) {
                log.warn("秒杀订单监听器:用户编号:{},商品id:{},已经存在,不可重复下单", userPhone, productId);
            }

            //todo 执行扣减库存 下单操作
        }
        return null;
    }
}
