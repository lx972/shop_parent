package cn.lx.shop.item.feign;

import cn.lx.shop.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * cn.lx.shop.item.feign
 *
 * @Author Administrator
 * @date 16:23
 */
@FeignClient(name = "item")
@RequestMapping(value = "/page")
public interface PageFeign {

    /**
     * 根据商品的spuid生成对应的静态页面
     * @param id
     * @return
     */
    @GetMapping(value = "/createHtml/{id}")
    Result createHtml(@PathVariable("id") String id);
}
