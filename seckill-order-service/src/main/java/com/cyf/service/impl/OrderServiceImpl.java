package com.cyf.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.mapper.OrderMapper;
import com.cyf.model.Order;
import com.cyf.service.OrderService;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Reference(version = "1.0.0")
    private ProductService productService;

    @Autowired
    private OrderMapper orderMapper;

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

        return this.page(page,wrapper);
    }
}
