package cn.lx.shop.order.service.impl;

import cn.lx.shop.goods.feign.SkuFeign;
import cn.lx.shop.goods.feign.SpuFeign;
import cn.lx.shop.goods.pojo.Sku;
import cn.lx.shop.goods.pojo.Spu;
import cn.lx.shop.order.pojo.OrderItem;
import cn.lx.shop.order.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * cn.lx.shop.order.service.impl
 *
 * @Author Administrator
 * @date 18:05
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加商品到购物车
     * @param username
     * @param skuId
     * @param num
     */
    @Override
    public void add(String username, String skuId, Integer num) {
        if(num<1){
            //移除购物车中的这个商品
            redisTemplate.boundHashOps("car_"+username).delete(skuId);
            return;
        }


        //根据商品id查询商品数据
        Sku sku = skuFeign.findById(skuId).getData();
        Spu spu = spuFeign.findById(sku.getSpuId().toString()).getData();
        //封装商品
        OrderItem item = getOrderItem(skuId, num, sku, spu);

        redisTemplate.boundHashOps("car_"+username).put(skuId,item);
    }

    /**
     * 购物车中读取商品信息
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> list = redisTemplate.boundHashOps("car_" + username).values();
        return list;
    }

    /**
     * 封装商品
     * @param skuId
     * @param num
     * @param sku
     * @param spu
     * @return
     */
    private OrderItem getOrderItem(String skuId, Integer num, Sku sku, Spu spu) {
        //代表购物车中某一个商品
        OrderItem item = new OrderItem();
        //数据封装
        item.setCategoryId1(spu.getCategory1Id());
        item.setCategoryId2(spu.getCategory2Id());
        item.setCategoryId3(spu.getCategory3Id());
        item.setSpuId(spu.getId().toString());
        item.setSkuId(skuId);
        item.setName(sku.getName());
        item.setPrice(sku.getPrice());
        item.setNum(num);
        item.setImage(sku.getImage());
        return item;
    }
}
