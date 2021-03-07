import com.cyf.msg.BaseMessages;
import com.cyf.msg.OrderMsgProtocol;
import org.junit.Test;

/**
 * @author 陈一锋
 * @date 2021/3/7 18:13
 **/
public class MessageTest {

    @Test
    public void t1(){

        BaseMessages orderMsgProtocol = OrderMsgProtocol.builder()
                .orderSn("2020111")
                .productId("22")
                .userPhone("15633332221")
                .chargeMoney("199")
                .build();

        orderMsgProtocol.setTopic("order_skill");

        String json = orderMsgProtocol.encode();
        System.out.println(json);
    }
}
