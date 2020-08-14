package cn.lx.shop.content.feign;

import cn.lx.shop.content.pojo.Content;
import cn.lx.shop.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * cn.lx.shop.content.feign
 *
 * @Author Administrator
 * @date 11:48
 */

@FeignClient(name = "content")
@RequestMapping(value = "/content")
public interface ContentFeign {

    /***
     * 根据分类id查询广告集合
     * @param id
     * @return
     */
    @GetMapping(value = "/list/category/{id}")
    Result<List<Content>> findByCategoryId(@PathVariable("id") Long id);
}
