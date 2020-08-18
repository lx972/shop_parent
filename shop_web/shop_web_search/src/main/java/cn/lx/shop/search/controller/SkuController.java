package cn.lx.shop.search.controller;

import cn.lx.shop.search.feign.SkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * cn.lx.shop.search.controller
 *
 * @Author Administrator
 * @date 15:03
 */
@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){
        Map search = skuFeign.search(searchMap);
        model.addAttribute("result",search);
        model.addAttribute("searchMap",searchMap);
        //将查询条件拼接到url中
        String url = handleUrl(searchMap);
        model.addAttribute("url",url);
        return "search";
    }

    /**
     * 将查询条件拼接到url中
     * @param searchMap
     * @return
     */
    private String handleUrl(@RequestParam(required = false) Map<String, String> searchMap) {
        StringBuffer stringBuffer=new StringBuffer("/search/list");
        String url =null;
        if (searchMap!=null&&searchMap.size()>0){
            stringBuffer.append("?");
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                //分页参数不用拼接
                if(!entry.getKey().equalsIgnoreCase("pageNum")&&!entry.getKey().equalsIgnoreCase("pageSize")){
                    stringBuffer.append(entry.getKey());
                    stringBuffer.append("=");
                    stringBuffer.append(entry.getValue());
                    stringBuffer.append("&");
                }
                //去掉最后一个&
                url = stringBuffer.substring(0, stringBuffer.length() - 1);
            }
        }
        return url;
    }
}
