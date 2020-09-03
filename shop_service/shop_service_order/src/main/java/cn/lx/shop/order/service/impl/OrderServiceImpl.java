package cn.lx.shop.order.service.impl;
import cn.lx.shop.entity.IdWorker;
import cn.lx.shop.goods.feign.SkuFeign;
import cn.lx.shop.goods.pojo.Sku;
import cn.lx.shop.order.dao.OrderItemMapper;
import cn.lx.shop.order.dao.OrderMapper;
import cn.lx.shop.order.pojo.Order;
import cn.lx.shop.order.pojo.OrderItem;
import cn.lx.shop.order.service.OrderService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:shenkunlin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 交换机的名字
     */
    @Value("${mq.order.directExchange}")
    private String directExchange;

    /**
     * 延时队列名
     */
    @Value("${mq.order.queue}")
    private String queue;

    /**
     * 生成订单
     * @param order
     */
    @GlobalTransactional
    @Override
    public void add(Order order){

        order.setId(String.valueOf(idWorker.nextId()));
        //取出购物车中的所有商品
        List<OrderItem> orderItems = redisTemplate.boundHashOps("car_" + order.getUsername()).values();
        //数量合计
        Integer totalNum=0;
        //金额合计
        double totalMoney=0;
        for (OrderItem orderItem : orderItems) {
            //判断是否是购物车中勾选的商品
            if (order.getSkuIds().contains(orderItem.getSkuId())){
                //判断是否异价，以sql中的价格为准
                Sku sku = skuFeign.findById(orderItem.getSkuId()).getData();
                totalNum+=orderItem.getNum();
                totalMoney+=sku.getPrice();
                orderItem.setPrice(sku.getPrice());
                orderItem.setOrderId(order.getId());
                orderItem.setId(String.valueOf(idWorker.nextId()));
                //将订单明细存入数据库中
                orderItemMapper.insertSelective(orderItem);
                //从购物车中移除该商品
                redisTemplate.boundHashOps("car_" + order.getUsername()).delete(orderItem.getSkuId());
                //递减库存
                skuFeign.decrCount(orderItem.getSkuId(),orderItem.getNum().toString());
            }

        }
        //数量合计
        order.setTotalNum(totalNum);
        //金额合计
        order.setTotalMoney(totalMoney);
        //实付金额
        order.setPayMoney(totalMoney);
        //优惠金额
        order.setPreMoney(totalMoney-totalMoney);
        //在线支付
        order.setPayType("1");
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        //1 买家已评价 0 未评价
        order.setBuyerRate("0");
        //订单来源 1 web
        order.setSourceType("1");
        //0 未完成 1 订单已完成
        order.setOrderStatus("0");
        //0 未支付
        order.setPayStatus("0");
        //0 未发货
        order.setConsignStatus("0");
        //0 未删除
        order.setIsDelete("0");
        orderMapper.insert(order);
        //该订单发送到mq
        rabbitTemplate.convertAndSend(queue,(Object) JSON.toJSONString(order.getId()),new MessagePostProcessor(){
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置超时时间
                message.getMessageProperties().setExpiration("1800000");
                return message;
            }
        });

    }

    /**
     * Order条件+分页查询
     * @param order 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order){
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     * @param order
     * @return
     */
    public Example createExample(Order order){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(order!=null){
            // 订单id
            if(!StringUtils.isEmpty(order.getId())){
                    criteria.andEqualTo("id",order.getId());
            }
            // 数量合计
            if(!StringUtils.isEmpty(order.getTotalNum())){
                    criteria.andEqualTo("totalNum",order.getTotalNum());
            }
            // 金额合计
            if(!StringUtils.isEmpty(order.getTotalMoney())){
                    criteria.andEqualTo("totalMoney",order.getTotalMoney());
            }
            // 优惠金额
            if(!StringUtils.isEmpty(order.getPreMoney())){
                    criteria.andEqualTo("preMoney",order.getPreMoney());
            }
            // 邮费
            if(!StringUtils.isEmpty(order.getPostFee())){
                    criteria.andEqualTo("postFee",order.getPostFee());
            }
            // 实付金额
            if(!StringUtils.isEmpty(order.getPayMoney())){
                    criteria.andEqualTo("payMoney",order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if(!StringUtils.isEmpty(order.getPayType())){
                    criteria.andEqualTo("payType",order.getPayType());
            }
            // 订单创建时间
            if(!StringUtils.isEmpty(order.getCreateTime())){
                    criteria.andEqualTo("createTime",order.getCreateTime());
            }
            // 订单更新时间
            if(!StringUtils.isEmpty(order.getUpdateTime())){
                    criteria.andEqualTo("updateTime",order.getUpdateTime());
            }
            // 付款时间
            if(!StringUtils.isEmpty(order.getPayTime())){
                    criteria.andEqualTo("payTime",order.getPayTime());
            }
            // 发货时间
            if(!StringUtils.isEmpty(order.getConsignTime())){
                    criteria.andEqualTo("consignTime",order.getConsignTime());
            }
            // 交易完成时间
            if(!StringUtils.isEmpty(order.getEndTime())){
                    criteria.andEqualTo("endTime",order.getEndTime());
            }
            // 交易关闭时间
            if(!StringUtils.isEmpty(order.getCloseTime())){
                    criteria.andEqualTo("closeTime",order.getCloseTime());
            }
            // 物流名称
            if(!StringUtils.isEmpty(order.getShippingName())){
                    criteria.andEqualTo("shippingName",order.getShippingName());
            }
            // 物流单号
            if(!StringUtils.isEmpty(order.getShippingCode())){
                    criteria.andEqualTo("shippingCode",order.getShippingCode());
            }
            // 用户名称
            if(!StringUtils.isEmpty(order.getUsername())){
                    criteria.andLike("username","%"+order.getUsername()+"%");
            }
            // 买家留言
            if(!StringUtils.isEmpty(order.getBuyerMessage())){
                    criteria.andEqualTo("buyerMessage",order.getBuyerMessage());
            }
            // 是否评价
            if(!StringUtils.isEmpty(order.getBuyerRate())){
                    criteria.andEqualTo("buyerRate",order.getBuyerRate());
            }
            // 收货人
            if(!StringUtils.isEmpty(order.getReceiverContact())){
                    criteria.andEqualTo("receiverContact",order.getReceiverContact());
            }
            // 收货人手机
            if(!StringUtils.isEmpty(order.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",order.getReceiverMobile());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(order.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(!StringUtils.isEmpty(order.getSourceType())){
                    criteria.andEqualTo("sourceType",order.getSourceType());
            }
            // 交易流水号
            if(!StringUtils.isEmpty(order.getTransactionId())){
                    criteria.andEqualTo("transactionId",order.getTransactionId());
            }
            // 订单状态
            if(!StringUtils.isEmpty(order.getOrderStatus())){
                    criteria.andEqualTo("orderStatus",order.getOrderStatus());
            }
            // 支付状态 0:未支付 1:已支付
            if(!StringUtils.isEmpty(order.getPayStatus())){
                    criteria.andEqualTo("payStatus",order.getPayStatus());
            }
            // 发货状态 0:未发货 1:已发货 2:已送达
            if(!StringUtils.isEmpty(order.getConsignStatus())){
                    criteria.andEqualTo("consignStatus",order.getConsignStatus());
            }
            // 是否删除
            if(!StringUtils.isEmpty(order.getIsDelete())){
                    criteria.andEqualTo("isDelete",order.getIsDelete());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Order
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }



    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 修改订单支付状态
     * @param resultMap
     * @return
     */
    @Override
    public int updatePayStatus(Map<String, String> resultMap) {
        Order order = new Order();
        order.setId(resultMap.get("out_trade_no"));
        //1为已支付
        order.setPayStatus("1");
        try {
            //支付完成时间
            order.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(resultMap.get("time_end")));
            //订单的更新时间
            order.setUpdateTime(order.getPayTime());
            //交易流水号
            order.setTransactionId(resultMap.get("transaction_id"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = orderMapper.insertSelective(order);
        return i;
    }

    /**
     * 逻辑删除订单
     * @param out_trade_no
     * @return
     */
    @Override
    public int logicDelete(String out_trade_no) {
        Order order = new Order();
        order.setId("out_trade_no");
        //1为删除
        order.setIsDelete("1");
        int i = orderMapper.insertSelective(order);
        //回滚库存
        OrderItem orderItem=new OrderItem();
        orderItem.setOrderId(out_trade_no);
        List<OrderItem> orderItemList = orderItemMapper.select(orderItem);
        for (OrderItem item : orderItemList) {
            skuFeign.rollBackInventory(item.getSkuId(),item.getNum().toString());
        }
        return i;
    }

    /**
     * 订单超时
     * @param orderId
     * @return
     */
    @Override
    public int orderTimeout(String orderId) {
        Order timeoutOrder = orderMapper.selectByPrimaryKey(orderId);
        if (!timeoutOrder.getPayStatus().equalsIgnoreCase("1")){
            //该订单未支付
            //将订单设置为关闭状态
            timeoutOrder.setOrderStatus("2");
            timeoutOrder.setCloseTime(new Date());
            int i = orderMapper.updateByPrimaryKeySelective(timeoutOrder);
            return i;
        }
        return 0;
    }
}
