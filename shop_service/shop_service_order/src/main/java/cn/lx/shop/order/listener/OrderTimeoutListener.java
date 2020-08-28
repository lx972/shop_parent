package cn.lx.shop.order.listener;

import cn.lx.shop.order.service.OrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OrderService orderService;


    /**
     * 监听超时订单的消息
     * @param message
     */
    @RabbitHandler
    public void listenerMessage(String message){
        String orderId = JSON.parseObject(message, String.class);
        orderService.orderTimeout(orderId);
    }
}
