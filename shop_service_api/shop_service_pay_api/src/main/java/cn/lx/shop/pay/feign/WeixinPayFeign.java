package cn.lx.shop.pay.feign;

import cn.lx.shop.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * cn.lx.shop.pay.feign
 *
 * @Author Administrator
 * @date 9:24
 */
@FeignClient(name = "pay")
@RequestMapping(value = "/weixin/pay")
public interface WeixinPayFeign {

    /**
     * 关闭微信订单
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/close/order")
    Result closeOrder(@PathVariable("out_trade_no") String out_trade_no);
}
