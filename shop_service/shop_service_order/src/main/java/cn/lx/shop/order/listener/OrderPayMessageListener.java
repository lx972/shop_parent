package cn.lx.shop.order.listener;

import cn.lx.shop.entity.Result;
import cn.lx.shop.order.service.OrderService;
import cn.lx.shop.wxpay.feign.WeixinPayFeign;
import cn.lx.shop.user.feign.UserFeign;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * cn.lx.shop.order.listener
 *
 * @Author Administrator
 * @date 17:34
 */
@Component
@RabbitListener(queues = {"${mq.pay.queue}"})
public class OrderPayMessageListener {

    @Autowired
    private OrderService orderService;


    @Autowired
    private WeixinPayFeign weixinPayFeign;


    @Autowired
    private UserFeign userFeign;


    /**
     * 接收消息
     * @param message
     */
    @RabbitHandler
    public void consumerMessgae(String message){
        Map<String,String> resultMap = JSON.parseObject(message, Map.class);
        if (resultMap.get("return_code").equalsIgnoreCase("SUCCESS")&&resultMap.get("result_code").equalsIgnoreCase("SUCCESS")){
            //用户付款成功
            int i=orderService.updatePayStatus(resultMap);
            //付款后增加用户积分
            userFeign.addUserPoints(resultMap.get("total_fee"));
            return;
        }
        //用户付款失败
        //关闭微信支付订单
        Result result = weixinPayFeign.closeOrder(resultMap.get("out_trade_no"));
        if (result.isFlag()&&result.getData()==null){
            //关闭微信支付订单成功
            //逻辑删除订单,并回滚库存
            int upd = orderService.logicDelete(resultMap.get("out_trade_no"));
        }else if (result.isFlag()&&result.getData()!=null){
            //关闭微信支付订单未成功
            switch (result.getMessage()){
                case "ORDERPAID":
                    //订单已支付
                    int i=orderService.updatePayStatus(resultMap);
                    break;
                case "ORDERCLOSED":
                    //订单已关闭，无法重复关闭
                    break;
            }
        }

    }
}
