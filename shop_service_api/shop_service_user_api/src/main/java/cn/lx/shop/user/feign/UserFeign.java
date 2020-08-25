package cn.lx.shop.user.feign;

import cn.lx.shop.entity.Result;
import cn.lx.shop.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * cn.lx.shop.user.feign
 *
 * @Author Administrator
 * @date 16:03
 */
@FeignClient(name = "user")
@RequestMapping(value = "/user")
public interface UserFeign {


    /***
     * 根据ID查询User数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<User> findById(@PathVariable String id);
}
