package cn.lx.shop.oauth.controller;

import cn.lx.shop.oauth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AuthService authService;
}
