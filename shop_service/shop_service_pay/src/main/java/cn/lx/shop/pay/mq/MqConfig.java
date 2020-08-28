package cn.lx.shop.pay.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * cn.lx.shop.pay.mq
 *
 * @Author Administrator
 * @date 16:33
 */
@Component
public class MqConfig {

    /**
     * 交换机的名字
     */
    @Value("${mq.pay.directExchange}")
    private String directExchange;


    /**
     * 队列的名字
     */
    @Value("${mq.pay.queue}")
    private String queue;

    /**
     * 指定发送的队列
     */
    @Value("${mq.pay.routingKey}")
    private String routingKey;


    /**
     * 创建一个交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(directExchange,true,false);
    }


    /**
     * 创建一个队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(queue,true);
    }


    /**
     * 队列绑定到交换机上
     * @return
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(routingKey);
    }
}
