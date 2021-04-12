package com.cyf.mq.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyf.enums.OrderEnum;
import com.cyf.enums.OrderType;
import com.cyf.model.Order;
import com.cyf.msg.CreateOrderMessage;
import com.cyf.service.OrderService;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 秒杀订单监听器
 * 实际逻辑处理者
 *
 * @author 陈一锋
 * @date 2021/3/18 22:23
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = CreateOrderMessage.TOPIC,
        consumerGroup = "${spring.application.name}-consumer-group")
public class SeckillOrderConsumer implements RocketMQListener<CreateOrderMessage> {

    @Autowired
    private OrderService orderService;

    @Reference(version = "1.0.0")
    private ProductService productService;


    @Override
    public void onMessage(CreateOrderMessage message) {

        log.info("秒杀订单监听器接收到消息,消息内容:{}", message.toString());

        //消息幂等:判断是否消费过
        String orderSn = message.getOrderSn();
        List<Order> list = orderService.list(new QueryWrapper<Order>().lambda().eq(Order::getOrderSn, orderSn));
        if (CollectionUtil.isNotEmpty(list)) {
            log.warn("秒杀订单监听器:订单编号:{} 该订单已经消费过");
            return;
        }

        //业务幂等:判断同一个商品id +userPhone只有一个订单
        String productId = message.getProId();
        String userPhone = message.getUserPhone();
        List<Order> businessList = orderService.list(new QueryWrapper<Order>()
                .lambda()
                .eq(Order::getProId, productId)
                .eq(Order::getUserPhone, userPhone));
        if (CollectionUtil.isNotEmpty(businessList)) {
            log.warn("秒杀订单监听器:用户编号:{},商品id:{},已经存在,不可重复下单", userPhone, productId);
            return;
        }

        Order order = new Order();
        order.setOrderStatus(OrderType.SECKILL.getType());
        //秒杀订单价格 这里简化不做任何优化扣减
        order.setMoney(new BigDecimal(message.getChargeMoney()));
        BeanUtil.copyProperties(message, order);

        //先作库存校验 看看还有没有 如果还有则执行下一步 没有则消费成功 返回给用户信息
        boolean b = productService.hasStock(productId);
        if (!b) {
            log.info("秒杀订单监听器:消息消费成功,商品id:{},库存已卖完!!");
            return;
        }

        log.info("用户:{},扣减商品id:{},存在库存,进行下单操作", userPhone, productId);
        Order orderResult = orderService.createOrder(order);

        if (orderResult != null) {
            log.info("用户:{},扣减商品id:{},下单成功!!!", userPhone, productId);
            // 模拟订单处理，直接修改订单状态为处理中
            orderService.updateStatus(OrderEnum.DOING.getStatus(), orderResult.getId());
        } else {
            throw new RuntimeException("秒杀订单监听器:下单失败,消息内容:" + message.toString());
        }

    }
}
