package com.cyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyf.domain.Product;

/**
 * @author 陈一锋
 * @date 2021/3/27 15:49
 **/
public interface ProductService extends IService<Product> {

    /**
     * 乐观锁扣减库存操作
     *
     * @param proId 商品id
     * @return 是否成功
     */
    boolean deStock(Long proId);

    /**
     * 指定key是否还有库存
     *
     * @param pid 商品id
     * @return /
     */
    boolean hasStock(String pid);
}
