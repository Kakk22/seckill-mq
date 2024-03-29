package com.cyf.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.enums.OrderEnum;
import com.cyf.event.CreateOrderEvent;
import com.cyf.mapper.OrderMapper;
import com.cyf.model.Order;
import com.cyf.service.OrderService;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 这里因为继承了ServiceImpl,此类为一个aop代理对象
 * 需添加interfaceClass = OrderService.class
 * 否则消费者找不到该服务
 *
 * @author 陈一锋
 * @date 2021/3/19 22:49
 **/
@Component
@Service(version = "1.0.0", interfaceClass = OrderService.class)
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService, ApplicationEventPublisherAware {


    @Reference(version = "1.0.0")
    private ProductService productService;

    @Autowired
    private OrderMapper orderMapper;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Order createOrder(Order order) {
        int proId = order.getProId();
        //扣减真实库存库存
        boolean result = productService.deStockByLock((long) proId);

        if (!result) {
            //失败可通知用户扣减失败,
            log.info("用户扣减库存失败,proId:{},用户手机号:{}", proId, order.getUserPhone());
            return null;
        }
        //扣减成功则执行下单
        order.setChargeTime(new Date());
        order.setUpdateTime(new Date());
        boolean saveResult = save(order);

        if (saveResult) {
            log.info("用户下单成功,下单实体:{}", order.toString());
            //发送创建订单成功事件
            CreateOrderEvent createOrderEvent = new CreateOrderEvent(this, order.getOrderSn());
            applicationEventPublisher.publishEvent(createOrderEvent);
            return order;
        } else {
            log.error("用户下单失败,下单实体:{}", order.toString());
            return null;
        }
    }

    @Override
    public void updateStatus(Integer status, Integer id) {
        orderMapper.updateStatus(status, id);
    }

    @Override
    public IPage<Order> list(String userPhone, Integer type, Integer pageSize, Integer PageNum) {
        //查询秒杀订单列表
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Order::getUserPhone, userPhone)
                .eq(Order::getOrderType, type)
                .eq(Order::getStatus, 0);

        IPage<Order> page = new Page<>(pageSize, PageNum);

        return this.page(page, wrapper);
    }


    @Override
    public void cancelOrder(String orderSn) {
        //查询该订单是否已支付
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Order::getOrderSn, orderSn)
                .eq(Order::getStatus, 0)
                .eq(Order::getPay, 0);

        List<Order> list = this.list(wrapper);
        if (CollectionUtil.isEmpty(list)) {
            log.info("订单:{}已经完成支付或已删除");
            return;
        }

        //不为空 则取消订单
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Order::getOrderSn, orderSn)
                .eq(Order::getStatus, 0)
                .eq(Order::getPay, 0)
                .set(Order::getOrderStatus, OrderEnum.CANCEL.getStatus())
                .set(Order::getUpdateTime, new Date());

        this.update(null, updateWrapper);
        log.info("取消订单成功,订单编号:{}", orderSn);
        //todo 释放库存
    }
}
