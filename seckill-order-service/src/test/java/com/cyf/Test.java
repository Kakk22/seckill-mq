package com.cyf;

import com.cyf.event.CreateOrderEvent;
import com.cyf.service.OrderService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 陈一锋
 * @date 2021/3/27 16:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test implements ApplicationEventPublisherAware {

    @Autowired
    private OrderService orderService;

    private ApplicationEventPublisher applicationEventPublisher;


    @org.junit.Test
    public void t1() {
        orderService.cancelOrder("6ebf3b4b-2546-48d7-b0ff-06be833b6214");
    }

    @org.junit.Test
    public void t2() {
        CreateOrderEvent createOrderEvent = new CreateOrderEvent(this, "d0ff4fdf-cfff-4461-b3f4-39911e374552");
        applicationEventPublisher.publishEvent(createOrderEvent);
        Thread.yield();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
