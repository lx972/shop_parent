package cn.lx.shop.user.feign;

import cn.lx.shop.entity.Result;
import cn.lx.shop.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 付款后增加用户积分
     * @param totalMoney
     * @return
     * @throws Exception
     */
    @PutMapping(value = "")
    Result addUserPoints(@RequestParam String totalMoney);
}
