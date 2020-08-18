package cn.lx.shop.goods.feign;

import cn.lx.shop.entity.Result;
import cn.lx.shop.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @param status
     * @return
     */
    @RequestMapping(value = "/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);
}
