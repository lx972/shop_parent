package cn.lx.shop.seckill.listener;

import cn.lx.shop.entity.Result;
import cn.lx.shop.pay.feign.WeixinPayFeign;
import cn.lx.shop.seckill.pojo.SeckillStatus;
import cn.lx.shop.seckill.service.SeckillOrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * cn.lx.shop.seckill.listener
 *
 * @Author Administrator
 * @date 16:02
 */
@Component
@RabbitListener(queues = "${mq.pay.seckillQueue}")
public class SeckillOrderPayMessageListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private WeixinPayFeign weixinPayFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 接收消息
     * @param message
     */
    @RabbitHandler
    public void consumerMessgae(String message){
        Map<String,String> resultMap = JSON.parseObject(message, Map.class);
        //附加数据
        String attach = resultMap.get("attach");
        Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
        if (resultMap.get("return_code").equalsIgnoreCase("SUCCESS")&&resultMap.get("result_code").equalsIgnoreCase("SUCCESS")){
            //支付成功
            int i = seckillOrderService.updatePayStatus(attachMap.get("username"), resultMap.get("time_end"), resultMap.get("transaction_id"));
            //抢单状态
            SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("SeckillStatus").get(attachMap.get("username"));
            //5:支付完成
            seckillStatus.setStatus("5");
            //抢单状态存入到redis中
            redisTemplate.boundHashOps("SeckillStatus").put(attachMap.get("username"),seckillStatus);
            return;
        }
        //用户付款失败
        //关闭微信支付订单
        Result result = weixinPayFeign.closeOrder(resultMap.get("out_trade_no"));
        if (result.isFlag()&&result.getData()==null){
            //关闭微信支付订单成功
            //删除订单,并回滚库存
           seckillOrderService.logicDelete(attachMap.get("username"));
        }

    }
}
