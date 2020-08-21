package cn.lx.shop.item.canal.listener;

import cn.lx.shop.item.feign.PageFeign;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * cn.lx.shop.canal.cn.lx.shop.item.canal.listener
 *
 * @Author Administrator
 * @date 16:30
 */
@CanalEventListener
public class CanalDataEventListener {


    @Autowired
    private PageFeign pageFeign;



    /**
     * 监听sku表的数据修改
     *
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example"
            , schema = "shop_goods"
            , table = {"tb_sku"}
            , eventType = {
            CanalEntry.EventType.DELETE,
            CanalEntry.EventType.INSERT,
            CanalEntry.EventType.UPDATE
    })
    public void onEventSkuItem(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //得到spuid
        String spuid = getColumnValue(eventType, rowData, "spu_id");
        //生成对应spuid的页面
        pageFeign.createHtml(spuid);
    }


    /**
     * 监听spu表的数据修改
     *
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example"
            , schema = "shop_goods"
            , table = {"tb_spu"}
            , eventType = {
            CanalEntry.EventType.DELETE,
            CanalEntry.EventType.INSERT,
            CanalEntry.EventType.UPDATE
    })
    public void onEventSpuItem(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //得到spuid
        String spuid = getColumnValue(eventType, rowData, "id");
        //生成对应spuid的页面
        pageFeign.createHtml(spuid);
    }


    /**
     * 获取数据库表指定字段的值
     * @param eventType
     * @param rowData
     * @param field
     * @return
     */
    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData,String field) {
        List<CanalEntry.Column> columnsList = null;
        if (eventType.equals(CanalEntry.EventType.DELETE)) {
            //删除一条商品数据进行的操作
            columnsList = rowData.getBeforeColumnsList();
        } else {
            //修改和添加
            columnsList = rowData.getAfterColumnsList();
        }
        //遍历获得spuid数据
        for (CanalEntry.Column column : columnsList) {
            if (column.getName().equalsIgnoreCase(field)) {
                return column.getValue();
            }
        }
        return "";
    }
}
