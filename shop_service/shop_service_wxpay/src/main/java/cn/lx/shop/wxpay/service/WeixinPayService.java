package cn.lx.shop.wxpay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * cn.lx.shop.wxpay.service.impl
 *
 * @Author Administrator
 * @date 10:33
 */
public interface WeixinPayService {

    /**
     * 统一下单
     * @param map
     * @return
     * @throws Exception
     */
    Map<String, String> createNative(Map<String, String> map,String username) throws Exception;

    /**
     * 查询订单状态
     * @param out_trade_no  订单号
     * @return
     * @throws Exception
     */
    Map<String, String> queryPayStatus(String out_trade_no) throws Exception;

    /**
     * 用户付款之后回调
     * @param request
     * @param response
     * @return
     */
    void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 关闭微信支付订单
     * @param out_trade_no
     * @return
     */
    Map<String, String> closeOrder(String out_trade_no) throws Exception;
}
