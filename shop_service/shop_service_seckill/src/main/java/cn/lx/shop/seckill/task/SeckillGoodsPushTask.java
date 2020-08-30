package cn.lx.shop.seckill.task;

import cn.lx.shop.entity.DateUtil;
import cn.lx.shop.seckill.dao.SeckillGoodsMapper;
import cn.lx.shop.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * cn.lx.shop.seckill.task
 *
 * @Author Administrator
 * @date 15:53
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;


    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis() {
        //得到秒杀商品的时间段
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date startTime : dateMenus) {
            //找到当前时间段的秒杀商品
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andGreaterThanOrEqualTo("startTime", startTime);
            criteria.andLessThan("endTime", DateUtil.addDateHour(startTime, 2));
            //已通过审核
            criteria.andEqualTo("status", "1");
            //数量大于0
            criteria.andGreaterThan("stockCount", 0);
            //得到当前时间段的开始时间的字符串
            String dateStr = DateUtil.data2str(startTime, "yyyyMMddHH");
            Set keys = redisTemplate.boundHashOps("seckillGoods_" + dateStr).keys();
            if (keys != null && !keys.isEmpty()) {
                //redis中已有的不用查
                criteria.andNotIn("itemId", keys);
            }
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);

            for (SeckillGoods seckillGoods : seckillGoodsList) {
                //设置过期时间
                redisTemplate.boundHashOps("seckillGoods_" + dateStr).expireAt(DateUtil.addDateHour(startTime, 2));
                //保存到redis中
                redisTemplate.boundHashOps("seckillGoods_" + dateStr).put(seckillGoods.getItemId().toString(), seckillGoods);
                //设置一个保存着skuid，长度为商品个数的队列，用来防止超卖
                redisTemplate.boundListOps("seckillGoodsCountQueue_"+seckillGoods.getItemId()).leftPushAll(pushIds(seckillGoods.getStockCount(),seckillGoods.getItemId().toString()));
                //设置自增计数器，记录商品剩余数量
                redisTemplate.boundHashOps("seckillGoodsCount_"+seckillGoods.getItemId()).increment(seckillGoods.getItemId().toString(),seckillGoods.getStockCount());
                //设置自增计数器，记录商品销量
                redisTemplate.boundHashOps("SalesVolume"+seckillGoods.getItemId()).increment(seckillGoods.getItemId().toString(),0);
            }
        }
    }


    /**
     * 创建一个保存着skuid，长度为商品个数的队列，用来防止超卖
     * @param num
     * @param skuId
     * @return
     */
    private String[] pushIds(Integer num,String skuId){
        String[] ids=new String[num];
        for (int i=0;i<num;i++){
            ids[i]=skuId;
        }
        return ids;
    }
}
