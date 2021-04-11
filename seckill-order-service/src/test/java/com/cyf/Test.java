package com.cyf;

import com.cyf.service.OrderService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 陈一锋
 * @date 2021/3/27 16:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private OrderService orderService;

    @org.junit.Test
    public void t1() {
        orderService.cancelOrder("6ebf3b4b-2546-48d7-b0ff-06be833b6214");
    }
}
