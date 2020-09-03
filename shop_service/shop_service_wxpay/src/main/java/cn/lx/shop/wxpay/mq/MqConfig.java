package cn.lx.shop.wxpay.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * cn.lx.shop.wxpay.mq
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
     * 秒杀的队列的名字
     */
    @Value("${mq.pay.seckillQueue}")
    private String seckillQueue;


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
     * 秒杀的队列的名字
     * @return
     */
    @Bean
    public Queue seckillQueue(){
        return new Queue(seckillQueue,true);
    }


    /**
     * 队列绑定到交换机上
     * @return
     */
    @Bean
    public Binding bindingQueue(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(queue);
    }



    /**
     * 秒杀队列绑定到交换机上
     * @return
     */
    @Bean
    public Binding bindingSeckillQueue(){
        return BindingBuilder.bind(seckillQueue()).to(directExchange()).with(seckillQueue);
    }
}
