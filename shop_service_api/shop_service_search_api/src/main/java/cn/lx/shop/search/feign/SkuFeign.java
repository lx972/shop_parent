package cn.lx.shop.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * cn.lx.shop.search.feign
 *
 * @Author Administrator
 * @date 14:55
 */
@FeignClient(name = "search")
@RequestMapping(value = "/search")
public interface SkuFeign {
    /**
     * 关键字搜索
     * @param map
     * @return
     */
    @GetMapping()
    Map search(@RequestParam(required = false) Map<String,String> map);
}
