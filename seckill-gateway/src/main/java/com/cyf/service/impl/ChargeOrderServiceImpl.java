package com.cyf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.cyf.constant.RedisKeyConstant;
import com.cyf.dto.ChargeOrderRequest;
import com.cyf.dto.ChargeOrderResponse;
import com.cyf.enums.MessagesProtocolEnum;
import com.cyf.mq.SecKillOrderProducer;
import com.cyf.msg.OrderMsgProtocol;
import com.cyf.service.ChargeOrderService;
import com.cyf.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈一锋
 * @date 2021/3/12 15:30
 **/
@Service
@Slf4j
public class ChargeOrderServiceImpl implements ChargeOrderService {

    @Resource
    private ProductService productService;

    @Resource
    private SecKillOrderProducer secKillOrderProducer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean chargeOrder(ChargeOrderRequest chargeOrderRequest) {

        //验证价格
        verifyPrice(chargeOrderRequest.getPrice());
        //商品校验 预减库存
        boolean result = productService.decreaseStock(chargeOrderRequest.getProductId(),chargeOrderRequest.getPhone());

        if (!result) {
            log.warn("秒杀订单扣减库存失败,请求参数:{}", chargeOrderRequest.toString());
            return false;
        }
        try {
            seckillOrderEnqueue(chargeOrderRequest);
        } catch (Exception e) {
            log.error("订单投递失败,回退库存,异常信息:{}", e.getMessage());
            redisTemplate.opsForValue().increment(RedisKeyConstant.PRODUCT_STOCK + chargeOrderRequest.getProductId());
            return false;
        }

        //消息投递成功后,记录该下单消息,该用户不能再扣减该库存
        String key = RedisKeyConstant.ORDER_RECORD_UID + chargeOrderRequest.getProductId();
        redisTemplate.opsForSet().add(key, chargeOrderRequest.getPhone());
        redisTemplate.expire(key,86400,TimeUnit.SECONDS);
        return true;
    }

    @Override
    public void seckillOrderEnqueue(ChargeOrderRequest chargeOrderRequest) throws Exception {
        //生成订单编号
        String orderSn = UUID.randomUUID().toString();
        //组装消息
        OrderMsgProtocol orderMsgProtocol = new OrderMsgProtocol();
        orderMsgProtocol.setUserPhone(chargeOrderRequest.getPhone());
        orderMsgProtocol.setChargeMoney(chargeOrderRequest.getPrice());
        orderMsgProtocol.setProId(chargeOrderRequest.getProductId());
        orderMsgProtocol.setOrderSn(orderSn);

        String msg = orderMsgProtocol.encode();
        log.info("秒杀消息入队,消息协议:{}", msg);

        DefaultMQProducer mqProducer = secKillOrderProducer.getProducer();
        //封装mq消息
        Message message = new Message(MessagesProtocolEnum.SECKILL_ORDER_TOPIC.getTopic(), msg.getBytes());

        try {
            SendResult result = mqProducer.send(message);
            //判断是否为空或状态不为成功
            if (result == null || result.getSendStatus() != SendStatus.SEND_OK) {
                log.error("秒杀订单消息入队失败,msgBody:{}", msg);
                //todo 封装返回值
            }
            //成功
            ChargeOrderResponse response = new ChargeOrderResponse();
            BeanUtil.copyProperties(orderMsgProtocol, response);
            //todo 返回成功信息

            log.info("秒杀订单入队成功,订单排队中。订单信息:{}", response);
        } catch (MQClientException | InterruptedException | MQBrokerException | RemotingException e) {
            log.error("秒杀消息入队失败,mq发生消息错误信息:{}", e.getMessage());
            throw e;
        }

    }

    private void verifyPrice(String price) {
        BigDecimal bigDecimal = new BigDecimal(price);
        if (bigDecimal.longValue() < 0) {
            throw new RuntimeException("订单价格不能小于0");
        }
    }
}


