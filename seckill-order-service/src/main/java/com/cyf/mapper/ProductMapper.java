package com.cyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyf.domain.Product;
import org.apache.ibatis.annotations.Param;


/**
 * 其实应该有个商品服务,先简略一下
 *
 * @author 陈一锋
 * @date 2021/3/12 16:06
 **/
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 扣减库存
     *
     * @param pid     商品id
     * @param version 版本号
     * @return 成功失败
     */
    boolean deStock(@Param("pid") Long pid, @Param("version") Integer version);
}
