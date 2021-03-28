package com.cyf.constant;

/**
 * 记录redis key
 *
 * @author 陈一锋
 * @date 2021/3/28 16:18
 **/
public interface RedisKeyConstant {

    /**
     * 当库存为0时,存入redis
     */
    String STOCK_ZERO = "stock:zero:pid:";
}
