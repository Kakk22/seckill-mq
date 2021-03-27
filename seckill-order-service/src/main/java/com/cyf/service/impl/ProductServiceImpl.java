package com.cyf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyf.mapper.ProductMapper;
import com.cyf.domain.Product;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public boolean deStock(Long proId) {
        //乐观锁扣减库存
        Long id = Optional.ofNullable(proId).orElseThrow(NullPointerException::new);
        Product product = getById(id);

        //判断库存是否充足
        if (product.getStock() == 0) {
            return false;
        }

        Integer version = product.getVersion();
        boolean b = productMapper.deStock(id, version);

        if (!b) {
            log.info("扣减库存失败,商品id:{}", id);
        } else {
            log.info("扣减库存成功,商品id:{},当前库存:{},版本号:{}", id, product.getStock() - 1, product.getVersion() + 1);

        }
        return b;
    }
}
