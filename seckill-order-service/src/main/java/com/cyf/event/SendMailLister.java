package com.cyf.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 创建订单成功发送邮件
 *
 * @author 陈一锋
 * @date 2021/4/10 22:38
 **/
@Component
public class SendMailLister implements ApplicationListener<CreateOrderEvent> {


    @Override
    public void onApplicationEvent(CreateOrderEvent event) {

    }
}
