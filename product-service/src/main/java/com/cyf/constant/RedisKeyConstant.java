package com.cyf.constant;

/**
 * @author 陈一锋
 * @date 2021/3/28 20:04
 **/
public interface RedisKeyConstant {


    /**
     * 商品库存key
     */
    String PRODUCT_STOCK = "product:stock:pid:";

    /**
     * set集合 key商品id value 用户集合
     */
    String ORDER_RECORD_UID = "order:record:pid:";

    /**
     * 当库存为0时,存入redis
     */
    String STOCK_ZERO = "stock:zero:pid:";
}
