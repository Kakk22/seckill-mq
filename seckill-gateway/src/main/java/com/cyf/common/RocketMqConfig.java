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

//    @Value("${rocketMq.nameServer.pro}")
    private String proNameServer;

    private static final String OFFLINE = "offline";

    private static final String ALIYUN = "aliyun";

    public String getNameServerAddress() {

        String envType = System.getProperty("envType");

        if (StringUtils.isBlank(envType)) {
            throw new IllegalArgumentException("please insert envType");
        }

        if (OFFLINE.equals(envType)) {
            return offlineNameServer;
        } else if (ALIYUN.equals(envType)) {
            return proNameServer;
        } else {
            throw new IllegalArgumentException("please insert right envType, offline/aliyun");
        }
    }
}
