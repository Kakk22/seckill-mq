package com.cyf.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 *
 * @author 陈一锋
 * @date 2021/3/19 22:45
 **/
@TableName(value = "t_order")
@Data
public class Order implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int proId;
    private String proName;
    private String orderSn;
    private int orderStatus;
    private String userPhone;
    private int status;
    private BigDecimal money;
    private Date chargeTime;
    private Date finishTime;
    private int orderType;
    private Date createTime;
    private Date updateTime;
    private int pay;
    private Date payTime;
    private int payType;
}
