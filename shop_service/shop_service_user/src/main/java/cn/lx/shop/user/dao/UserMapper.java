package cn.lx.shop.user.dao;
import cn.lx.shop.user.pojo.User;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:shenkunlin
 * @Description:User的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface UserMapper extends Mapper<User> {

    /**
     * 付款后增加用户积分
     * @param username
     * @param totalMoney
     * @return
     */
    @Update("update tb_user set points=points+#{totalMoney} where username=#{username}")
    int addUserPoints(@RequestParam("username") String username,@RequestParam("totalMoney") String totalMoney);
}
