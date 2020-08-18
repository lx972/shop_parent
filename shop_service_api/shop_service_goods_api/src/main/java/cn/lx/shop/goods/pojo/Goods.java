package cn.lx.shop.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * cn.lx.shop.goods.pojo
 *
 * @Author Administrator
 * @date 15:46
 */
public class Goods implements Serializable {

    private Spu spu;

    private List<Sku> skuList;

    public Goods() {
    }

    public Goods(Spu spu, List<Sku> skuList) {
        this.spu = spu;
        this.skuList = skuList;
    }

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
