package com.cyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyf.model.Product;
import org.apache.ibatis.annotations.Param;

/**
 * @author 陈一锋
 * @date 2021/3/12 16:06
 **/
public interface ProductMapper extends BaseMapper<Product> {


    /**
     * 扣减真实库存
     * @param id /
     * @return /
     */
    boolean deStockByLock(@Param("pid") Long id);
}
