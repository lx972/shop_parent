package cn.lx.shop.order.service;

import cn.lx.shop.order.pojo.OrderItem;

import java.util.List;


/**
 * cn.lx.shop.order.service
 *
 * @Author Administrator
 * @date 18:05
 */
public interface CarService {

    /**
     * 添加商品到购物车
     * @param username
     * @param skuId
     * @param num
     */
    void add(String username, String skuId, Integer num);

    /**
     * 购物车中读取商品信息
     * @param username
     * @return
     */
    List<OrderItem> list(String username);
}
