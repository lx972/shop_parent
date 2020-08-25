package cn.lx.shop.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * cn.lx.shop.oauth.controller
 *
 * @Author Administrator
 * @date 10:24
 */
@Controller
@RequestMapping(value = "/oauth")
public class ModelController {

    /***
     * 跳转到登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login(@RequestParam(required = false) String from, Model model){
        model.addAttribute("from",from);
        return "login";
    }
}
