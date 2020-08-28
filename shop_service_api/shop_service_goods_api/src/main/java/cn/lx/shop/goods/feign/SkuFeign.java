package cn.lx.shop.goods.feign;

import cn.lx.shop.entity.Result;
import cn.lx.shop.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * cn.lx.shop.goods.feign
 *
 * @Author Administrator
 * @date 12:07
 */
@FeignClient(name = "goods")
@RequestMapping(value = "/sku")
public interface SkuFeign {

    /**
     * 查询审核通过的Sku数据
     *
     * @param status
     * @return
     */
    @GetMapping(value = "/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);


    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable String id);


    /**
     * 下单后更新商品库存
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping(value = "/decr/count")
    Result decrCount(@RequestParam String skuId,@RequestParam String num);


    /**
     * 订单支付失败后回滚库存
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping(value = "/roll/back/inventory")
    public Result rollBackInventory(@RequestParam String skuId,@RequestParam String num);

}
