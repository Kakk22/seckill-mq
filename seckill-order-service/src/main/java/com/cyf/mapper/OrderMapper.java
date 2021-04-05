package com.cyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyf.model.Order;
import org.apache.ibatis.annotations.Param;

/**
 * @author 陈一锋
 * @date 2021/3/19 22:49
 **/
public interface OrderMapper extends BaseMapper<Order> {



    void updateStatus(@Param("status") Integer status, @Param("id") Integer id);
}
