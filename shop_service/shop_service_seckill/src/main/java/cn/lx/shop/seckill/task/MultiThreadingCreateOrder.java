package cn.lx.shop.seckill.task;

import cn.lx.shop.entity.IdWorker;
import cn.lx.shop.seckill.dao.SeckillGoodsMapper;
import cn.lx.shop.seckill.pojo.SeckillGoods;
import cn.lx.shop.seckill.pojo.SeckillOrder;
import cn.lx.shop.seckill.pojo.SeckillStatus;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * cn.lx.shop.seckill.task
 *
 * @Author Administrator
 * @date 9:33
 */
@Component
public class MultiThreadingCreateOrder {


    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 延时队列名
     */
    @Value("${mq.order.queue}")
    private String queue;

    /**
     * 多线程下单
     */
    @Async
    public void createOrder() {
        //从redis的排序列表中取出一个秒杀数据
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
        try {
            System.out.println("开始睡眠。。。");
            Thread.sleep(10000);
            if (seckillStatus != null) {
                String username=seckillStatus.getUsername();
                String skuId=seckillStatus.getSkuId();
                String substringTime = seckillStatus.getStartTime().substring(0, 10);
                //这种方式不能解决超卖问题
                /*SeckillGoods seckillGood = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods_" + substringTime).get(skuId);
                if (seckillGood == null || seckillGood.getStockCount() <= 0) {
                    throw new RuntimeException("该秒杀商品已经售完了");
                }*/
                Object flag = redisTemplate.boundListOps("seckillGoodsCountQueue_" + skuId).rightPop();
                if (flag==null){
                    //说明没有商品了
                    //删除记录用户重复抢单的redis
                    redisTemplate.boundHashOps("UserQueueCount").delete(username);
                    //删除记录用户抢单状态的redis
                    redisTemplate.boundHashOps("SeckillStatus").delete(username);
                    //退出线程
                    return;
                }

                //取出对应的商品数据
                SeckillGoods seckillGood = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods_" + substringTime).get(skuId);
                //创建秒杀订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                //秒杀商品的id(skuid)
                seckillOrder.setSeckillId(seckillGood.getItemId());
                //支付金额
                seckillOrder.setMoney(seckillGood.getCostPrice());
                //用户id
                seckillOrder.setUserId(username);
                //商家
                seckillOrder.setSellerId(seckillGood.getSellerId());
                //创建时间
                seckillOrder.setCreateTime(new Date());
                //未支付
                seckillOrder.setStatus("0");
                //将秒杀商品订单存入到redis中
                redisTemplate.boundHashOps("userOrders").put(username, seckillOrder);
                //减少库存
                Long surplusCount = redisTemplate.boundHashOps("seckillGoodsCount_" + skuId).increment(skuId, -1);
                //不要直接使用 seckillGood.setStockCount(seckillGood.getetStockCount()-1);,并发情况下不精确
                seckillGood.setStockCount(surplusCount.intValue());
                //增加销量
                Long salesVolume = redisTemplate.boundHashOps("SalesVolume" + skuId).increment(skuId, 1);
                seckillGood.setNum(salesVolume.intValue());
                if (surplusCount <= 0) {
                    //没有库存了，同步数据到mysql中
                    seckillGoodsMapper.updateByPrimaryKeySelective(seckillGood);
                    //删除redis中的该秒杀商品信息
                    redisTemplate.boundHashOps("seckillGoods_" + substringTime).delete(seckillGood.getItemId().toString());
                }else {
                    //同步数据到redis中
                    redisTemplate.boundHashOps("seckillGoods_" + substringTime).put(seckillGood.getItemId().toString(), seckillGood);
                }

                //抢单成功，修改抢单的状态,2:秒杀等待支付
                seckillStatus.setStatus("2");
                //订单id
                seckillStatus.setOrderId(seckillOrder.getId().toString());
                //付款金额
                seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
                //抢单状态存入到redis中
                redisTemplate.boundHashOps("SeckillStatus").put(username,seckillStatus);
                //向rabbit发送一个消息30分钟后才能被监听到
                rabbitTemplate.convertAndSend(queue,(Object) JSON.toJSONString(seckillStatus),new MessagePostProcessor(){
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //设置超时时间
                        message.getMessageProperties().setExpiration("10000");
                        return message;
                    }
                });
            }
            System.out.println("抢单成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
