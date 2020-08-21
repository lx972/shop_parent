package cn.lx.shop.goods.feign;

import cn.lx.shop.entity.Result;
import cn.lx.shop.goods.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * cn.lx.shop.goods.feign
 *
 * @Author Administrator
 * @date 16:10
 */
@FeignClient(name = "goods")
@RequestMapping(value = "/category")
public interface CategoryFeign {

    /***
     * 根据ID查询Category数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Category> findById(@PathVariable Integer id);
}
