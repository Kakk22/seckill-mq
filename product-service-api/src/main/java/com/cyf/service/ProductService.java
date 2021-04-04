package com.cyf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyf.model.Product;

/**
 * 商品服务接口
 *
 * @author 陈一锋
 * @date 2021/4/4 10:09
 **/
public interface ProductService {


    /**
     * 更新商品库存入redis
     */
    void initProduct();

    /**
     * 分页获取商品信息
     *
     * @param pageSize 每页大小
     * @param pageNum  当前页
     * @return 商品列表
     */
    IPage<Product> list(Integer pageSize, Integer pageNum);

    /**
     * 扣减redis库存
     *
     * @param productId 商品id
     * @return 是否成功
     */
    boolean decreaseStock(String productId, String phone);

    /**
     * 扣减真实库存
     *
     * @param id 商品id
     * @return /
     */
    boolean deStockByLock(Long id);

    /**
     * 指定key是否还有库存
     *
     * @param pid 商品id
     * @return /
     */
    boolean hasStock(String pid);

}
