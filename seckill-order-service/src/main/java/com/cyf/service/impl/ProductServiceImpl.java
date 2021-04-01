package com.cyf.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.constant.RedisKeyConstant;
import com.cyf.domain.Product;
import com.cyf.mapper.ProductMapper;
import com.cyf.service.ProductService;
import com.cyf.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author 陈一锋
 * @date 2021/3/27 15:53
 **/
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    @Override
    public boolean deStock(Long proId) {
        //乐观锁扣减库存
        Long id = Optional.ofNullable(proId).orElseThrow(NullPointerException::new);
        Product product = getById(id);

        //判断库存是否充足
        if (product.getStock() == 0) {
            redisTemplate.opsForValue().set(RedisKeyConstant.STOCK_ZERO + proId, 0);
            return false;
        }

        String key = "product:lock:pid:" + proId;
        String uuid = UUID.randomUUID().toString();
        boolean lock = false;
        try {

            //获取分布式锁
            lock = redisLockService.lock(key, uuid, 20);

            if (lock) {
                log.info("获取分布式锁开始扣减库存,当前线程:{}", Thread.currentThread().getName());
                productMapper.deStockByLock(id);
                return true;
            }

            return false;

        } finally {
            //解锁
            if (lock) {
                redisLockService.unLock(key, uuid);
            }
        }
    }


    @Override
    public boolean hasStock(String pid) {

        if (StrUtil.isBlank(pid)) {
            log.error("查询库存参数错误,pid:{}", pid);
            return false;
        }

        //查询是否有该商品标记库存信息为0
        Object o = redisTemplate.opsForValue().get(RedisKeyConstant.STOCK_ZERO + pid);

        if (o != null) {
            log.info("商品pid:{},库存为0", pid);
            return false;
        }

        Product product = getById(Long.parseLong(pid));

        if (product == null || product.getStock() <= 0) {
            redisTemplate.opsForValue().set(RedisKeyConstant.STOCK_ZERO + pid, 0);
            log.info("商品pid:{}库存为0,信息存入redis", pid);
            return false;
        }

        log.info("商品pid:{},库存为{}", pid, product.getStock());
        return true;
    }
}
