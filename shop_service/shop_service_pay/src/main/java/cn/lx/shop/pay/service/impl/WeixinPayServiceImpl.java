package cn.lx.shop.pay.service.impl;

import cn.lx.shop.entity.HttpClient;
import cn.lx.shop.pay.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * cn.lx.shop.pay.service.impl
 *
 * @Author Administrator
 * @date 10:33
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    /**
     * 公众账号id
     */
    @Value("${weixin.appid}")
    private String appid;

    /**
     * 商户号
     */
    @Value("${weixin.mch_id}")
    private String mch_id;

    /**
     * 签名的盐
     */
    @Value("${weixin.signKey}")
    private String signKey;

    /**
     * 通知地址
     */
    @Value("${weixin.notify_url}")
    private String notify_url;



    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 统一下单
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> createNative(Map<String, String> map,String username) throws Exception {
        //下单参数封装
        String mapToXml = handleMap(map,username);
        String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
        Map<String, String> resultMap = sendToUrl(mapToXml, url);
        return resultMap;

    }

    /**
     * 模拟浏览器行为，向微信服务器发送请求
     * @param mapToXml
     * @param url
     * @return
     * @throws Exception
     */
    private Map<String, String> sendToUrl(String mapToXml, String url) throws Exception {
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.setXmlParam(mapToXml);
        httpClient.post();
        String resultXml = httpClient.getContent();
        return WXPayUtil.xmlToMap(resultXml);
    }

    /**
     * 下单参数封装
     * @param map
     * @param username
     * @return
     * @throws Exception
     */
    private String handleMap(Map<String, String> map,String username) throws Exception {
        //公众账号id
        map.put("appid",appid);
        //商户号
        map.put("mch_id",mch_id);
        //通知地址
        map.put("notify_url",notify_url);
        //设备号
        map.put("device_info","huawei");
        //随机字符串
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        //终端ip
        map.put("spbill_create_ip", "192.168.43.33");
        //商品描述 	body
        map.put("body", "腾讯充值中心-QQ会员充值");
        //交易起始时间
        map.put("time_start", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        //交易类型
        map.put("trade_type", "NATIVE");
        //附加参数
        Map<String,String> attach=new HashMap<>(16);
        attach.put("exchange",map.get("exchange"));
        attach.put("queue",map.get("queue"));
        attach.put("username",username);
        map.put("attach", JSON.toJSONString(attach));
        //删除map中的mq的相关参数
        map.remove("exchange");
        map.remove("queue");
        //签名
        String signature = WXPayUtil.generateSignature(map, signKey);
        map.put("sign", signature);
        return WXPayUtil.mapToXml(map);
    }

    /**
     * 查询订单状态
     * @param out_trade_no  订单号
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) throws Exception {

        Map<String,String> map=new HashMap<>();
        //公众账号id
        map.put("appid",appid);
        //商户号
        map.put("mch_id",mch_id);
        //订单号
        map.put("out_trade_no",out_trade_no);
        //随机字符串
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名
        String signature = WXPayUtil.generateSignature(map, signKey);
        map.put("sign", signature);
        String mapToXml=WXPayUtil.mapToXml(map);
        String url="https://api.mch.weixin.qq.com/pay/orderquery";
        Map<String, String> resultMap = sendToUrl(mapToXml, url);
        return resultMap;
    }

    /**
     * 用户付款之后回调
     * @param request
     * @param response
     * @return
     */
    @Override
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //微信的的通知信息在里面
        ServletInputStream is = request.getInputStream();
        byte[] bytes=new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len=0;
        while ((len=is.read(bytes))!=-1){
            bos.write(bytes,0,len);
        }
        String resultXml = new String(bos.toByteArray(), "UTF-8");
        bos.close();
        is.close();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
        //得到附加参数
        String attach = resultMap.get("attach");
        Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
        //回应微信，已收到通知，不需要继续发送
        Map<String, String> resultToWX =new HashMap<>(16);
        resultToWX.put("return_code","SUCCESS");
        resultToWX.put("return_msg","OK");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(WXPayUtil.mapToXml(resultToWX).getBytes());
        //向mq发送消息
        rabbitTemplate.convertAndSend(attachMap.get("exchange"),attachMap.get("queue"), JSON.toJSONString(resultMap));
        //return resultMap;
    }

    /**
     * 关闭微信支付订单
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> closeOrder(String out_trade_no) throws Exception {
        String url="https://api.mch.weixin.qq.com/pay/closeorder";
        Map<String,String> wxMap=new HashMap<>(16);
        wxMap.put("appid",appid);
        wxMap.put("mch_id",mch_id);
        wxMap.put("out_trade_no",out_trade_no);
        wxMap.put("nonce_str", WXPayUtil.generateNonceStr());
        String signature = WXPayUtil.generateSignature(wxMap, signKey);
        wxMap.put("sign",signature);
        String mapToXml = WXPayUtil.mapToXml(wxMap);
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.setXmlParam(mapToXml);
        httpClient.post();
        String result = httpClient.getContent();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
        return resultMap;
    }
}
