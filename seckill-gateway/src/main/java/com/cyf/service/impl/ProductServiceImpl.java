package com.cyf.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.constant.RedisKeyConstant;
import com.cyf.domain.Product;
import com.cyf.mapper.ProductMapper;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈一锋
 * @date 2021/3/12 16:49
 **/
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


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
            log.error("商品信息校验失败,商品id:{}", productId);
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

        if (afterStock == null || afterStock <= 0) {
            log.warn("商品库存为0,商品id:{}", productId);
            return false;
        }
        //存在库存 扣除成功
        log.info("商品预扣除库存成功,商品id:{},商品剩余库存:{}", productId, afterStock);
        return true;

    }
}
