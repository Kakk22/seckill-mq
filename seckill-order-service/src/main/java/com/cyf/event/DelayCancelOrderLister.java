package com.cyf.event;

import com.cyf.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
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
@Component
public class DelayCancelOrderLister implements ApplicationListener<CreateOrderEvent> {



    @Override
    public void onApplicationEvent(CreateOrderEvent event) {

    }
}
