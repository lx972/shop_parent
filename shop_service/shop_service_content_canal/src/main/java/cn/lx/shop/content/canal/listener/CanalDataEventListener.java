package cn.lx.shop.content.canal.listener;

import cn.lx.shop.content.feign.ContentFeign;
import cn.lx.shop.content.pojo.Content;
import cn.lx.shop.entity.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

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
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ContentFeign contentFeign;


    @ListenPoint(destination = "example",
            schema = "shop_content",
            table = {"tb_content_category"},
            eventType = {
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.INSERT
            })
    public void onEventCategory(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach(column -> System.out.println(column.getName()+":"+column.getValue()));
    }

    /**
     * 自定义的监听操作，更新数据到redis
     * 广告
     *
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example",
            schema = "shop_content",
            table = {"tb_content"},
            eventType = {
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.INSERT
            })
    public void onEventContent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String category_id = getColumnValue(eventType, rowData,"category_id");
        //调用feign获取该分类下的所有广告集合
        Result<List<Content>> result = contentFeign.findByCategoryId(Long.valueOf(category_id));
        //得到广告数据
        List<Content> contents = result.getData();
        //将信息更新到redis中
        stringRedisTemplate.boundValueOps("content_" + category_id).set(JSON.toJSONString(contents));
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
        //遍历获得field数据
        for (CanalEntry.Column column : columnsList) {
            if (column.getName().equalsIgnoreCase(field)) {
                System.out.println(column.getName());
                return column.getValue();
            }
        }
        return "";
    }
}
