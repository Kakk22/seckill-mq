package com.cyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyf.domain.Product;

/**
 * 商品服务
 *
 * @author 陈一锋
 * @date 2021/3/12 16:45
 **/
public interface ProductService extends IService<Product> {

    /**
     * 扣减库存
     *
     * @param productId 商品id
     * @return 是否成功
     */
    boolean decreaseStock(String productId,String phone);
}
