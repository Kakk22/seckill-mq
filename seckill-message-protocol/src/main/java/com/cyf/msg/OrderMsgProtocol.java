package com.cyf.msg;

import cn.hutool.core.util.StrUtil;
import com.cyf.enums.MessagesProtocolEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.cyf.constant.MessageConstant.*;

/**
 * @author 陈一锋
 * @date 2021/3/7 17:27
 **/
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class OrderMsgProtocol extends BaseMessages implements Serializable {
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 用户手机
     */
    private String userPhone;
    /**
     * 商品id
     */
    private String proId;
    /**
     * 订单价格
     */
    private String chargeMoney;
    /**
     * 消息头
     */
    private Map<String, String> header;
    /**
     * 消息体
     */
    private Map<String, String> body;


    @Override
    public String encode() {
        //构建头数据
        header = new HashMap<String, String>(2);
        header.put(VERSION, this.getVersion());
        header.put(TOPIC, MessagesProtocolEnum.SECKILL_ORDER_TOPIC.getTopic());

        //构建body
        body = new HashMap<String, String>(4);
        body.put(ORDER_SN, this.orderSn);
        body.put(USER_PHONE, this.userPhone);
        body.put(PRODUCT_ID, this.proId);
        body.put(CHARGE_MONEY, this.chargeMoney);

        HashMap<String, Object> data = new HashMap<String, Object>(2);
        data.put(HEADER, header);
        data.put(BODY, body);

        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("The orderMsgProtocol messages encode fail,error:{}", e.getMessage());
        }

        return result;
    }

    @Override
    public void decode(String msg) {

        if (StrUtil.isBlank(msg)) {
            throw new NullPointerException("orderMsgProtocol is null");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            JsonNode node = objectMapper.readTree(msg);

            //头信息
            this.setVersion(node.get(HEADER).get(VERSION).asText());
            this.setTopic(node.get(HEADER).get(TOPIC).asText());

            //体信息
            JsonNode body = node.get(BODY);
            this.setOrderSn(body.get(ORDER_SN).asText());
            this.setUserPhone(body.get(USER_PHONE).asText());
            this.setProId(body.get(PRODUCT_ID).asText());
            this.setChargeMoney(body.get(CHARGE_MONEY).asText());

        } catch (JsonProcessingException e) {
            log.error("The orderMsgProtocol decode fail,error:{}", e.getMessage());
        }
    }


}
