package cn.lx.shop.seckill.service.impl;
import cn.lx.shop.entity.IdWorker;
import cn.lx.shop.seckill.dao.SeckillGoodsMapper;
import cn.lx.shop.seckill.pojo.SeckillGoods;
import cn.lx.shop.seckill.pojo.SeckillStatus;
import cn.lx.shop.seckill.service.SeckillGoodsService;
import cn.lx.shop.seckill.task.MultiThreadingCreateOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
/****
 * @Author:shenkunlin
 * @Description:SeckillGoods业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;


    /**
     * SeckillGoods条件+分页查询
     * @param seckillGoods 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillGoods);
        //执行搜索
        return new PageInfo<SeckillGoods>(seckillGoodsMapper.selectByExample(example));
    }

    /**
     * SeckillGoods分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillGoods> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillGoods>(seckillGoodsMapper.selectAll());
    }

    /**
     * SeckillGoods条件查询
     * @param seckillGoods
     * @return
     */
    @Override
    public List<SeckillGoods> findList(SeckillGoods seckillGoods){
        //构建查询条件
        Example example = createExample(seckillGoods);
        //根据构建的条件查询数据
        return seckillGoodsMapper.selectByExample(example);
    }


    /**
     * SeckillGoods构建查询对象
     * @param seckillGoods
     * @return
     */
    public Example createExample(SeckillGoods seckillGoods){
        Example example=new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillGoods!=null){
            //
            if(!StringUtils.isEmpty(seckillGoods.getId())){
                    criteria.andEqualTo("id",seckillGoods.getId());
            }
            // spu ID
            if(!StringUtils.isEmpty(seckillGoods.getGoodsId())){
                    criteria.andEqualTo("goodsId",seckillGoods.getGoodsId());
            }
            // sku ID
            if(!StringUtils.isEmpty(seckillGoods.getItemId())){
                    criteria.andEqualTo("itemId",seckillGoods.getItemId());
            }
            // 标题
            if(!StringUtils.isEmpty(seckillGoods.getTitle())){
                    criteria.andLike("title","%"+seckillGoods.getTitle()+"%");
            }
            // 商品图片
            if(!StringUtils.isEmpty(seckillGoods.getSmallPic())){
                    criteria.andEqualTo("smallPic",seckillGoods.getSmallPic());
            }
            // 原价格
            if(!StringUtils.isEmpty(seckillGoods.getPrice())){
                    criteria.andEqualTo("price",seckillGoods.getPrice());
            }
            // 秒杀价格
            if(!StringUtils.isEmpty(seckillGoods.getCostPrice())){
                    criteria.andEqualTo("costPrice",seckillGoods.getCostPrice());
            }
            // 商家ID
            if(!StringUtils.isEmpty(seckillGoods.getSellerId())){
                    criteria.andEqualTo("sellerId",seckillGoods.getSellerId());
            }
            // 添加日期
            if(!StringUtils.isEmpty(seckillGoods.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillGoods.getCreateTime());
            }
            // 审核日期
            if(!StringUtils.isEmpty(seckillGoods.getCheckTime())){
                    criteria.andEqualTo("checkTime",seckillGoods.getCheckTime());
            }
            // 审核状态，0未审核，1审核通过，2审核不通过
            if(!StringUtils.isEmpty(seckillGoods.getStatus())){
                    criteria.andEqualTo("status",seckillGoods.getStatus());
            }
            // 开始时间
            if(!StringUtils.isEmpty(seckillGoods.getStartTime())){
                    criteria.andEqualTo("startTime",seckillGoods.getStartTime());
            }
            // 结束时间
            if(!StringUtils.isEmpty(seckillGoods.getEndTime())){
                    criteria.andEqualTo("endTime",seckillGoods.getEndTime());
            }
            // 秒杀商品数
            if(!StringUtils.isEmpty(seckillGoods.getNum())){
                    criteria.andEqualTo("num",seckillGoods.getNum());
            }
            // 剩余库存数
            if(!StringUtils.isEmpty(seckillGoods.getStockCount())){
                    criteria.andEqualTo("stockCount",seckillGoods.getStockCount());
            }
            // 描述
            if(!StringUtils.isEmpty(seckillGoods.getIntroduction())){
                    criteria.andEqualTo("introduction",seckillGoods.getIntroduction());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        seckillGoodsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillGoods
     * @param seckillGoods
     */
    @Override
    public void update(SeckillGoods seckillGoods){
        seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
    }

    /**
     * 增加SeckillGoods
     * @param seckillGoods
     */
    @Override
    public void add(SeckillGoods seckillGoods){
        seckillGoodsMapper.insert(seckillGoods);
    }

    /**
     * 根据ID查询SeckillGoods
     * @param id
     * @return
     */
    @Override
    public SeckillGoods findById(Long id){
        return  seckillGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillGoods全部数据
     * @return
     */
    @Override
    public List<SeckillGoods> findAll() {
        return seckillGoodsMapper.selectAll();
    }

    /**
     * 获取对应秒杀时间的商品列表
     * @param seckillStartTime
     * @return
     */
    @Override
    public List<SeckillGoods> list(String seckillStartTime) {
        String substringTime = seckillStartTime.substring(0, 10);
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods_" + substringTime).values();
        return seckillGoodsList;
    }


    /**
     * 查询秒杀商品的详情
     *
     * @param seckillStartTime
     * @param skuId
     * @return
     */
    @Override
    public SeckillGoods getRedisGoodById(String seckillStartTime, String skuId) {
        String substringTime = seckillStartTime.substring(0, 10);
        SeckillGoods seckillGood = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods_" + substringTime).get(skuId);
        return seckillGood;
    }


    /**
     * 下单
     *
     * @param seckillStartTime
     * @param skuId
     * @param username
     * @return
     */
    @Override
    public void userOrder(String seckillStartTime, String skuId,String username) {
        //递增，判断用户是否已经抢购过该商品（抢购未完成前，不允许抢购别的商品）
        Long userQueueCount = redisTemplate.boundHashOps("UserQueueCount").increment(username, 1);
        if (userQueueCount>1){
            //重复抢单
            throw new RuntimeException("用户已经抢购过该商品");
        }
        //封装排队消息
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), "1", skuId, seckillStartTime);
        //放入redis的list中排队
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);
        //抢单状态存入到redis中
        redisTemplate.boundHashOps("SeckillStatus").put(username,seckillStatus);
        //用户下单,多线程抢单
        multiThreadingCreateOrder.createOrder();
    }


    /**
     * 查询用户抢购状态
     * @param username
     * @return
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps("SeckillStatus").get(username);
        return seckillStatus;

    }
}
