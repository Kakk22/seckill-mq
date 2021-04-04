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

}
