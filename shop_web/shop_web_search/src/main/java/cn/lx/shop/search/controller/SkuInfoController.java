package cn.lx.shop.search.controller;

import cn.lx.shop.entity.Page;
import cn.lx.shop.search.feign.SkuInfoFeign;
import cn.lx.shop.search.pojo.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * cn.lx.shop.search.cn.lx.shop.user.controller
 *
 * @Author Administrator
 * @date 15:03
 */
@Controller
@RequestMapping(value = "/search")
public class SkuInfoController {

    @Autowired
    private SkuInfoFeign skuInfoFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){
        Map result = skuInfoFeign.search(searchMap);
        model.addAttribute("result",result);
        model.addAttribute("searchMap",searchMap);
        //将查询条件拼接到url中
        String[] urls = handleUrl(searchMap);
        //分页及条件搜索用url
        model.addAttribute("url",urls[0]);
        //排序用的url
        model.addAttribute("sortUrl",urls[1]);
        //处理分页
        Integer pageNum = (Integer) result.get("pageNum");
        Integer pageSize = (Integer) result.get("pageSize");
        Long total = Long.valueOf(result.get("total").toString());
        Page<SkuInfo> page=new Page<SkuInfo>(total,pageNum,pageSize);
        model.addAttribute("page",page);
        return "search";
    }

    /**
     * 将查询条件拼接到url中
     * @param searchMap
     * @return  [1] 不带排序参数，带分页的url
     * @return  [0] 带排序参数，不带分页的url
     */
    private String[] handleUrl(@RequestParam(required = false) Map<String, String> searchMap) {
        //分页及条件搜索用url
        StringBuffer urlBuffer=new StringBuffer("/search/list");
        //排序用的url
        StringBuffer sortUrlBuffer=new StringBuffer("/search/list");
        String url =null;
        String sortUrl =null;
        if (searchMap!=null&&searchMap.size()>0){
            urlBuffer.append("?");
            sortUrlBuffer.append("?");
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                //分页参数不用拼接,拼接了排序
                if(!entry.getKey().equalsIgnoreCase("pageNum")&&!entry.getKey().equalsIgnoreCase("pageSize")){
                    urlBuffer.append(entry.getKey());
                    urlBuffer.append("=");
                    urlBuffer.append(entry.getValue());
                    urlBuffer.append("&");

                    //排序参数不用拼接
                    if (!entry.getKey().equalsIgnoreCase("sortRule")&&!entry.getKey().equalsIgnoreCase("sortField")){
                        sortUrlBuffer.append(entry.getKey());
                        sortUrlBuffer.append("=");
                        sortUrlBuffer.append(entry.getValue());
                        sortUrlBuffer.append("&");
                    }
                }

                //去掉最后一个&
                url = urlBuffer.substring(0, urlBuffer.length() - 1);
                sortUrl = sortUrlBuffer.substring(0, sortUrlBuffer.length() - 1);
            }
        }
        String[] urls = new String[2];
        urls[0]=url;
        urls[1]=sortUrl;
        return urls;
    }
}
