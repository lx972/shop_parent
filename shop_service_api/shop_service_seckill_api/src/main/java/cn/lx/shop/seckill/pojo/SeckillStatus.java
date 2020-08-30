package cn.lx.shop.seckill.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * cn.lx.shop.seckill.pojo
 *
 * @Author Administrator
 * @date 16:16
 */
public class SeckillStatus implements Serializable {
    //秒杀用户名
    private String username;
    //创建时间
    private Date createTime;
    //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
    private String status;
    //秒杀的商品ID
    private String skuId;

    //应付金额
    private Float money;

    //订单号
    private String orderId;
    //时间段
    private String startTime;

    public SeckillStatus() {
    }

    public SeckillStatus(String username, Date createTime, String status, String skuId, String startTime) {
        this.username = username;
        this.createTime = createTime;
        this.status = status;
        this.skuId = skuId;
        this.startTime = startTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
