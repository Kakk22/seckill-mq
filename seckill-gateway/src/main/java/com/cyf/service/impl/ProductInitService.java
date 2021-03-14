package com.cyf.service.impl;

import com.alibaba.fastjson.JSON;
import com.cyf.domain.Product;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品初始化加载进redis中
 *
 * @author 陈一锋
 * @date 2021/3/14 19:26
 **/
@Slf4j
@Service
public class ProductInitService implements InitializingBean {

    @Resource
    private ProductService productService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Product> products = productService.list();

        if (CollectionUtils.isEmpty(products)) {
            log.warn("初始化订单失败,秒杀订单为空");
            return;
        }

        products.forEach(product -> {
                    String key = String.valueOf(product.getId());
                    redisTemplate.opsForValue().set(key, product, 84600, TimeUnit.SECONDS);
                }
        );
        log.info("初始化订单成功,商品信息:{}", JSON.toJSONString(products));
    }
}
