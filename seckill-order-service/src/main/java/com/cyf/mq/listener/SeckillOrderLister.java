package com.cyf.mq.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyf.domain.Order;
import com.cyf.enums.OrderEnum;
import com.cyf.msg.OrderMsgProtocol;
import com.cyf.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.RECONSUME_LATER;

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

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;



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
            List<Order> list = orderService.list(new QueryWrapper<Order>().lambda().eq(Order::getOrderSn, orderSn));
            if (CollectionUtil.isNotEmpty(list)) {
                log.warn("秒杀订单监听器:订单编号:{} 该订单已经消费过");
                return CONSUME_SUCCESS;
            }

            //业务幂等:判断同一个商品id +userPhone只有一个订单
            String productId = orderMsgProtocol.getProId();
            String userPhone = orderMsgProtocol.getUserPhone();
            List<Order> businessList = orderService.list(new QueryWrapper<Order>()
                    .lambda()
                    .eq(Order::getProId, productId)
                    .eq(Order::getUserPhone, userPhone));
            if (CollectionUtil.isNotEmpty(businessList)) {
                log.warn("秒杀订单监听器:用户编号:{},商品id:{},已经存在,不可重复下单", userPhone, productId);
                return CONSUME_SUCCESS;
            }

            Order order = new Order();
            BeanUtil.copyProperties(orderMsgProtocol, order);

            //先作库存校验 看看还有没有 如果还有则执行下一步 没有则消费成功 返回给用户信息
            boolean b = productService.hasStock(productId);
            if (!b) {
                log.info("秒杀订单监听器:消息消费成功,商品id:{},库存已卖完!!");
                return CONSUME_SUCCESS;
            }

            log.info("用户:{},扣减商品id:{},存在库存,进行下单操作", userPhone, productId);
            Order orderResult = orderService.createOrder(order);

            if (orderResult != null) {
                log.info("用户:{},扣减商品id:{},下单成功!!!", userPhone, productId);
                // 模拟订单处理，直接修改订单状态为处理中
                orderService.updateStatus(OrderEnum.DOING.getStatus(), orderResult.getId());
                return CONSUME_SUCCESS;
            }
            //扣减失败
            return RECONSUME_LATER;
        }
        return CONSUME_SUCCESS;
    }
}
