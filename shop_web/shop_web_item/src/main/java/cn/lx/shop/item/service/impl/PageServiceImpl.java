package cn.lx.shop.item.service.impl;

import cn.lx.shop.entity.Result;
import cn.lx.shop.goods.feign.CategoryFeign;
import cn.lx.shop.goods.feign.SkuFeign;
import cn.lx.shop.goods.feign.SpuFeign;
import cn.lx.shop.goods.pojo.Category;
import cn.lx.shop.goods.pojo.Sku;
import cn.lx.shop.goods.pojo.Spu;
import cn.lx.shop.item.service.PageService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cn.lx.shop.item.service.impl
 *
 * @Author Administrator
 * @date 14:58
 */
@Service
public class PageServiceImpl implements PageService {

    @Value("${pagePath}")
    private String pagePath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private CategoryFeign categoryFeign;


    /**
     * 根据商品的spuid生成对应的静态页面
     * @param spuid
     */
    @Override
    public void createHtml(String spuid) {
        System.out.println(pagePath);
        //得到Thymeleaf的上下文环境
        Context context = new Context();
        //将数据查询出来
        Map<String, Object> data = handleData(spuid);
        //发送给上下文环境，填充到页面
        context.setVariables(data);
        File file = new File(pagePath, spuid + ".html");
        try {
            PrintWriter printWriter = new PrintWriter(file);

            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 封装页面所需的数据
     * @param spuid
     * @return
     */
    private Map<String, Object> handleData(String spuid) {
        //声明一个map，用来保存需要发送到页面上的数据
        Map<String,Object> data=new HashMap<>(16);
        //得到同种的商品的信息
        Result<Spu> spuResult = spuFeign.findById(spuid);
        Spu spu = spuResult.getData();
        data.put("spu",spu);
        //图片列表
        String images = spu.getImages();
        data.put("spuImageList",images.split(","));
        //规格
        String specItems = spu.getSpecItems();
        Map specInfo = JSON.parseObject(specItems, Map.class);
        data.put("specification",specInfo);
        //商品参数
        Map paraInfo = JSON.parseObject(spu.getParaItems(), Map.class);
        data.put("paraInfo",paraInfo);
        Category category1 = categoryFeign.findById(spu.getCategory1Id()).getData();
        data.put("category1",category1);
        Category category2 = categoryFeign.findById(spu.getCategory2Id()).getData();
        data.put("category2",category2);
        Category category3 = categoryFeign.findById(spu.getCategory3Id()).getData();
        data.put("category3",category3);
        //根据spu得到每个商品sku信息
        Sku skuSearch = new Sku();
        skuSearch.setSpuId(Long.valueOf(spuid));
        Result<List<Sku>> skuResult = skuFeign.findList(skuSearch);
        List<Sku> skuList = skuResult.getData();
        data.put("skuList",skuList);
        return data;
    }
}
