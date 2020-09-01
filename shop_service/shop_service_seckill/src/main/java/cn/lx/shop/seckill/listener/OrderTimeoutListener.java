package cn.lx.shop.seckill.listener;

import cn.lx.shop.entity.Result;
import cn.lx.shop.pay.feign.WeixinPayFeign;
import cn.lx.shop.seckill.pojo.SeckillOrder;
import cn.lx.shop.seckill.pojo.SeckillStatus;
import cn.lx.shop.seckill.service.SeckillOrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * cn.lx.shop.order.listener
 *
 * @Author Administrator
 * @date 10:44
 */
@Component
@RabbitListener(queues = "${mq.order.deadLetterQueue}")
public class OrderTimeoutListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private WeixinPayFeign weixinPayFeign;


    /**
     * 监听超时订单的消息
     * @param message
     */
    @RabbitHandler
    public void listenerMessage(String message){
        //获取订单状态
        SeckillStatus seckillStatus = JSON.parseObject(message, SeckillStatus.class);
        String username=seckillStatus.getUsername();
        //获取秒杀商品订单
        SeckillOrder userOrder = (SeckillOrder) redisTemplate.boundHashOps("userOrders").get(username);
        if(userOrder!=null){
            //有订单信息，说明用户没有支付
            //关闭微信支付订单
            Result result = weixinPayFeign.closeOrder(seckillStatus.getOrderId());
            if (result.isFlag()&&result.getData()==null){
                //关闭微信支付订单成功
                //删除订单,并回滚库存
                seckillOrderService.logicDelete(username);
            }
        }

    }
}
