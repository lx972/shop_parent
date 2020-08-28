package cn.lx.shop.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * cn.lx.shop.order.mqConfig
 *
 * @Author Administrator
 * @date 10:10
 */
@Component
public class MqConfig {

    /**
     * 交换机的名字
     */
    @Value("${mq.order.directExchange}")
    private String directExchange;

    /**
     * 接收超时消息的队列名字
     */
    @Value("${mq.order.deadLetterQueue}")
    private String deadLetterQueue;

    /**
     * 延时队列名
     */
    @Value("${mq.order.queue}")
    private String queue;

    /**
     * 创建一个交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(directExchange);
    }

    /**
     * 创建一个延时队列，并设置消息超时后发送的队列
     * @return
     */
    @Bean
    public Queue queue(){
        return QueueBuilder.durable(queue)
                .withArgument("x-dead-letter-exchange", directExchange)
                .withArgument("x-dead-letter-routing-key", deadLetterQueue)
                .build();
    }

    /**
     * 创建一个死信队列，用来接收过期消息
     * @return
     */
    @Bean
    public Queue deadLetterQueue(){
        return new Queue(deadLetterQueue,true);
    }


    /**
     * 绑定接收过期消息的队列与交换机
     * @return
     */
    @Bean
    public Binding bindingQueue(){
        return BindingBuilder.bind(deadLetterQueue()).to(directExchange()).with(deadLetterQueue);
    }
}
