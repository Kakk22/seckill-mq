package com.cyf.msg;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 封装抽象消息
 *
 * @author 陈一锋
 * @date 2021/3/7 17:18
 **/
@Slf4j
@Data
public abstract class BaseMessages {

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * topic路由名称
     */
    private String topic;

    /**
     * 编码方法 子类实现
     *
     * @return 加密后消息
     */
    public abstract String encode();

    /**
     * 解码方法 子类实现
     *
     * @param msg 需解码消息
     */
    public abstract void decode(String msg);

}
