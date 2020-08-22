package cn.lx.shop.item.controller;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * cn.lx.shop.item.cn.lx.shop.user.controller
 *
 * @Author Administrator
 * @date 14:54
 */
@RestController
@RequestMapping(value = "/page")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 根据商品的spuid生成对应的静态页面
     * @param id
     * @return
     */
    @GetMapping(value = "/createHtml/{id}")
    public Result createHtml(@PathVariable("id") String id){
        pageService.createHtml(id);
        return new Result(true, StatusCode.OK,"生成静态页面成功");
    }
}
