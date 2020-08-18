package cn.lx.shop.search.service;

import java.util.Map;

/**
 * cn.lx.shop.search.service
 *
 * @Author Administrator
 * @date 12:11
 */
public interface SkuesService {

    /**
     * 导入sku数据到elasticsearch
     */
    void importSku();


    /**
     * 关键字搜索
     * @param searchMap
     * @return
     */
    Map search(Map<String,String> searchMap);
}
