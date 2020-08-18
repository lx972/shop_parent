package cn.lx.shop.search.controller;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.search.service.SkuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * cn.lx.shop.search.controller
 *
 * @Author Administrator
 * @date 14:59
 */
@RestController
@RequestMapping(value = "/search")
@CrossOrigin
public class SkuesController {

    @Autowired
    private SkuesService skuesService;

    /**
     * es数据导入
     * @return
     */
    @GetMapping(value = "/import")
    public Result importEs(){
        skuesService.importSku();
        return new Result(true, StatusCode.OK,"导入数据到es成功");
    }


    /**
     * 关键字搜索
     * @param map
     * @return
     */
    @GetMapping()
    public Map search(@RequestParam(required = false) Map<String,String> map){
        Map search = skuesService.search(map);
        search.put("flag",true);
        search.put("code",StatusCode.OK);
        search.put("message","关键字搜索成功");
        return search;
    }
}
