package cn.lx.shop.goods.feign;

import cn.lx.shop.entity.Result;
import cn.lx.shop.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * cn.lx.shop.goods.feign
 *
 * @Author Administrator
 * @date 15:34
 */
@FeignClient(name = "goods")
@RequestMapping(value = "/spu")
public interface SpuFeign {

    /**
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Spu> findById(@PathVariable String id);
}
