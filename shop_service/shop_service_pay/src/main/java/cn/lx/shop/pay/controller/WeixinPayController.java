package cn.lx.shop.pay.controller;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * cn.lx.shop.pay.controller
 *
 * @Author Administrator
 * @date 10:30
 */
@RestController
@RequestMapping(value = "/weixin/pay")
public class WeixinPayController {


    @Autowired
    private WeixinPayService weixinPayService;


    /**
     * 关闭微信订单
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/close/order/{out_trade_no}")
    public Result closeOrder(@PathVariable("out_trade_no") String out_trade_no) throws Exception {
       Map<String,String> resultMap=weixinPayService.closeOrder(out_trade_no);
       if (resultMap.get("return_code").equalsIgnoreCase("SUCCESS")){
           if (resultMap.get("result_code").equalsIgnoreCase("SUCCESS")){
               return new Result(true, StatusCode.OK,"关闭微信订单成功");
           }else {
               return new Result(true, StatusCode.OK,resultMap.get("err_code_des"),resultMap.get("err_code"));
           }
       }
        return new Result(false, StatusCode.ERROR,resultMap.get("return_msg"));
    }

    /**
     * 用户付款之后回调
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notify/url")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
       weixinPayService.notifyUrl(request,response);
        //return new Result(true, StatusCode.OK,"用户付款之后回调成功",resultMap);
    }

    /**
     * 统一下单
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/create/native")
    public Result createNative(@RequestBody Map<String,String> map) throws Exception {
        Map<String,String> resultMap= weixinPayService.createNative(map);
        return new Result(true, StatusCode.OK,"生成二维码成功",resultMap);
    }

    /**
     * 查询订单状态
     * @param out_trade_no  订单号
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/query/status")
    public Result queryPayStatus(@RequestParam String out_trade_no) throws Exception {
        Map<String,String> resultMap= weixinPayService.queryPayStatus(out_trade_no);
        return new Result(true, StatusCode.OK,"查询订单状态成功",resultMap);
    }
}
