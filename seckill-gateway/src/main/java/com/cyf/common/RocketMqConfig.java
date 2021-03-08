package com.cyf.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * rocketMQ 环境配置
 * 根据启动读取环境
 *
 * @author 陈一锋
 * @date 2021/3/7 22:11
 **/
@Component
public class RocketMqConfig {

    @Value("${rocketMq.nameServer.offline}")
    private String offlineNameServer;


    public String getNameServerAddress() {
        return offlineNameServer;
    }
}
