package com.cyf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyf.constant.RedisKeyConstant;
import com.cyf.mapper.ProductMapper;
import com.cyf.model.Product;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈一锋
 * @date 2021/4/4 10:22
 **/
@Slf4j
@Service(version = "1.0.0")
@Component
public class ProductServiceImpl implements ProductService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void initProduct() {
        List<Product> products = productMapper.selectList(null);

        if (CollectionUtils.isEmpty(products)) {
            log.warn("初始化订单失败,秒杀订单为空");
            return;
        }

        products.forEach(product -> {
                    String key = String.valueOf(product.getId());
                    redisTemplate.opsForValue().set(RedisKeyConstant.PRODUCT_STOCK + key, product.getStock(), 84600, TimeUnit.SECONDS);
                }
        );
        log.info("初始化订单成功,商品信息:{}", JSON.toJSONString(products));
    }

    @Override
    public IPage<Product> list(Integer pageSize, Integer pageNum) {
        IPage<Product> iPage = new Page<>(pageNum, pageSize);
        return productMapper.selectPage(iPage, null);
    }
}
