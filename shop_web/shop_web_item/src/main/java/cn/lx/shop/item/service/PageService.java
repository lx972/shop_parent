package cn.lx.shop.item.service;

/**
 * cn.lx.shop.item.service
 *
 * @Author Administrator
 * @date 14:57
 */
public interface PageService {

    /**
     * 根据商品的spuid生成对应的静态页面
     * @param spuid
     */
    void createHtml(String spuid);
}
