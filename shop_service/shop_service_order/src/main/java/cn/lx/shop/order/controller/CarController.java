package cn.lx.shop.order.controller;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.entity.TokenDecode;
import cn.lx.shop.order.pojo.OrderItem;
import cn.lx.shop.order.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * cn.lx.shop.order.controller
 *
 * @Author Administrator
 * @date 18:04
 */
@RestController
@RequestMapping(value = "/car")
public class CarController {

    @Autowired
    private CarService carService;


    /**
     * 购物车中读取商品信息
     * @return
     */
    @PostMapping(value = "/list")
    public Result list(){
        //用户信息从令牌中获取
        String username= TokenDecode.decodeToken().get("username");
        List<OrderItem> list=carService.list(username);
        return new Result(true, StatusCode.OK,"购物车中读取商品信息成功",list);
    }


    /**
     * 添加商品到购物车
     * @param skuId
     * @param num
     * @return
     */
    @PostMapping(value = "/add")
    public Result add(String skuId,Integer num){
        //用户信息从令牌中获取
        String username=TokenDecode.decodeToken().get("username");
        carService.add(username,skuId,num);
        return new Result(true, StatusCode.OK,"添加购物车成功");
    }

}
