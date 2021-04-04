package com.cyf.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyf.constant.RedisKeyConstant;
import com.cyf.mapper.ProductMapper;
import com.cyf.model.Product;
import com.cyf.service.ProductService;
import com.cyf.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private RedisLockService redisLockService;

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
        return productMapper.selectPage(iPage, new QueryWrapper<>());
    }

    @Override
    public boolean decreaseStock(String productId, String phone) {
        //验证参数
        if (StrUtil.isBlank(productId)) {
            log.error("商品服务扣减库存失败,商品id参数错误");
            return false;
        }

        //获取库存信息
        Integer stock = (Integer) redisTemplate.opsForValue().get(RedisKeyConstant.PRODUCT_STOCK + productId);

        if (stock == null || stock <= 0) {
            log.info("商品已售关~,商品id:{}", productId);
            return false;
        }
        //校验用户是否已经扣减库存
        String key = RedisKeyConstant.ORDER_RECORD_UID + productId;

        Boolean result = redisTemplate.opsForSet().isMember(key, phone);
        if (result == null || result) {
            log.info("用户:{},已经预扣减库存成功,商品id:{}请勿重复操作", phone, productId);
            return false;
        }

        //扣减库存
        Long afterStock = redisTemplate.opsForValue().decrement(RedisKeyConstant.PRODUCT_STOCK + productId);

        if (afterStock == null || afterStock < 0) {
            log.warn("商品库存为0,商品id:{}", productId);
            return false;
        }
        //存在库存 扣除成功
        log.info("商品预扣除库存成功,商品id:{},商品剩余库存:{}", productId, afterStock);
        return true;
    }

    @Override
    public boolean deStockByLock(Long proId) {
        //乐观锁扣减库存
        Long id = Optional.ofNullable(proId).orElseThrow(NullPointerException::new);
        Product product = productMapper.selectById(id);

        //判断库存是否充足
        if (product.getStock() == 0) {
            redisTemplate.opsForValue().set(RedisKeyConstant.PRODUCT_STOCK + proId, 0);
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

        Product product = productMapper.selectById(Long.parseLong(pid));

        if (product == null || product.getStock() <= 0) {
            redisTemplate.opsForValue().set(RedisKeyConstant.STOCK_ZERO  + pid, 0);
            log.info("商品pid:{}库存为0,信息存入redis", pid);
            return false;
        }

        log.info("商品pid:{},库存为{}", pid, product.getStock());
        return true;
    }


}
