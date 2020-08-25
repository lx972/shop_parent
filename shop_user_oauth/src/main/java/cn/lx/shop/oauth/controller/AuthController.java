package cn.lx.shop.oauth.controller;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.oauth.service.AuthService;
import cn.lx.shop.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * cn.lx.shop.oauth.controller
 *
 * @Author Administrator
 * @date 18:05
 */
@RestController
@RequestMapping(value = "/user")
public class AuthController {

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.grant_type}")
    private String grant_type;


    @Autowired
    private AuthService authService;

    /**
     * 授权认证方法
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(String username,String password) throws Exception {
        if (username==null){
            throw new RuntimeException("用户名不能为空");
        }
        if (password==null){
            throw new RuntimeException("密码不能为空");
        }
        AuthToken authToken = authService.login(username, password, clientId, clientSecret, grant_type);

        return new Result(true, StatusCode.OK,"登录成功",authToken);
    }
}
