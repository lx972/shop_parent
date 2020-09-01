package cn.lx.shop.seckill.service.impl;
import cn.lx.shop.seckill.dao.SeckillGoodsMapper;
import cn.lx.shop.seckill.dao.SeckillOrderMapper;
import cn.lx.shop.seckill.pojo.SeckillGoods;
import cn.lx.shop.seckill.pojo.SeckillOrder;
import cn.lx.shop.seckill.pojo.SeckillStatus;
import cn.lx.shop.seckill.service.SeckillOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.List;
/****
 * @Author:shenkunlin
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 更新订单支付状态
     *
     * @param username
     * @param time_end
     * @param transaction_id
     * @return
     */
    @Override
    public int updatePayStatus(String username, String time_end, String transaction_id) {
        try {
            SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("userOrders").get(username);
            seckillOrder.setStatus("1");
            seckillOrder.setPayTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(time_end));
            seckillOrder.setTransactionId(transaction_id);
            //订单存入mysql中
            int i = seckillOrderMapper.insertSelective(seckillOrder);
            //删除redis中的订单
            redisTemplate.boundHashOps("userOrders").delete(username);
            //删除抢单状态
            redisTemplate.boundHashOps("SeckillStatus").delete(username);
            //删除用户的重复抢购记录
            redisTemplate.boundHashOps("UserQueueCount").delete(username);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * SeckillOrder条件+分页查询
     *
     * @param seckillOrder 查询条件
     * @param page         页码
     * @param size         页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     *
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder) {
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     *
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder) {
        Example example = new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (seckillOrder != null) {
            // 主键
            if (!StringUtils.isEmpty(seckillOrder.getId())) {
                criteria.andEqualTo("id", seckillOrder.getId());
            }
            // 秒杀商品ID
            if (!StringUtils.isEmpty(seckillOrder.getSeckillId())) {
                criteria.andEqualTo("seckillId", seckillOrder.getSeckillId());
            }
            // 支付金额
            if (!StringUtils.isEmpty(seckillOrder.getMoney())) {
                criteria.andEqualTo("money", seckillOrder.getMoney());
            }
            // 用户
            if (!StringUtils.isEmpty(seckillOrder.getUserId())) {
                criteria.andEqualTo("userId", seckillOrder.getUserId());
            }
            // 商家
            if (!StringUtils.isEmpty(seckillOrder.getSellerId())) {
                criteria.andEqualTo("sellerId", seckillOrder.getSellerId());
            }
            // 创建时间
            if (!StringUtils.isEmpty(seckillOrder.getCreateTime())) {
                criteria.andEqualTo("createTime", seckillOrder.getCreateTime());
            }
            // 支付时间
            if (!StringUtils.isEmpty(seckillOrder.getPayTime())) {
                criteria.andEqualTo("payTime", seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if (!StringUtils.isEmpty(seckillOrder.getStatus())) {
                criteria.andEqualTo("status", seckillOrder.getStatus());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(seckillOrder.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if (!StringUtils.isEmpty(seckillOrder.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", seckillOrder.getReceiverMobile());
            }
            // 收货人
            if (!StringUtils.isEmpty(seckillOrder.getReceiver())) {
                criteria.andEqualTo("receiver", seckillOrder.getReceiver());
            }
            // 交易流水
            if (!StringUtils.isEmpty(seckillOrder.getTransactionId())) {
                criteria.andEqualTo("transactionId", seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     *
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder) {
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 增加SeckillOrder
     *
     * @param seckillOrder
     */
    @Override
    public void add(SeckillOrder seckillOrder) {
        seckillOrderMapper.insert(seckillOrder);
    }

    /**
     * 根据ID查询SeckillOrder
     *
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(Long id) {
        return seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillOrder全部数据
     *
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }

    /**
     * 删除订单，并回滚库存
     *
     * @param username
     * @return
     */
    @Override
    public void logicDelete(String username) {
        //获取用户的秒杀状态
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("SeckillStatus").get(username);
        String skuId = seckillStatus.getSkuId();
        //获取用户的订单
        SeckillOrder userOrder = (SeckillOrder) redisTemplate.boundHashOps("userOrders").get(username);
        if (userOrder != null && seckillStatus != null) {
            //未支付
            //删除用户的订单
            redisTemplate.boundHashOps("userOrders").delete(username);
            //去redis中查询该商品的数据
            SeckillGoods seckillGood = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods_" + seckillStatus.getStartTime()).get(skuId);
            if (seckillGood == null) {
                //没库存了，从数据库中加载
                SeckillGoods selectCondition = new SeckillGoods();
                selectCondition.setItemId(Long.valueOf(skuId));
                seckillGood = seckillGoodsMapper.selectOne(selectCondition);
            }
            //删除抢单状态
            redisTemplate.boundHashOps("SeckillStatus").delete(username);
            //删除用户的重复抢购记录
            redisTemplate.boundHashOps("UserQueueCount").delete(username);
            //回滚库存
            //商品数量加1
            redisTemplate.boundListOps("seckillGoodsCountQueue_" + skuId).leftPush(skuId);
            //自增计数器，商品剩余数量+1
            Long surplusCount = redisTemplate.boundHashOps("seckillGoodsCount_" + skuId).increment(skuId, 1);
            //自增计数器，记录商品销量-1
            Long salesVolume = redisTemplate.boundHashOps("SalesVolume" + skuId).increment(skuId, -1);
            seckillGood.setStockCount(surplusCount.intValue());
            seckillGood.setNum(salesVolume.intValue());
            //库存同步到redis中
            redisTemplate.boundHashOps("seckillGoods_" + seckillStatus.getStartTime()).put(skuId,seckillGood);
        }
    }
}
