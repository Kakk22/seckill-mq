package com.cyf.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 陈一锋
 * @date 2021/4/10 22:28
 **/
@Getter
public class CreateOrderEvent extends ApplicationEvent {

    private String orderSn;

    public CreateOrderEvent(Object source, String orderSn) {
        super(source);
        this.orderSn = orderSn;
    }

}
