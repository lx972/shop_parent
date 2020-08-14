package cn.lx.shop.canal.listener;

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
 * cn.lx.shop.canal.listener
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

    /**
     * 修改数据监听
     * @param eventType
     * @param rowData
     */
    @UpdateListenPoint
   public void onEventUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        rowData.getAfterColumnsList().forEach(column -> System.out.println("修改数据监听:"+column.getName()+":"+column.getValue()));
   }


   /**
     * 增加数据监听
     * @param eventType
     * @param rowData
     */
   @InsertListenPoint
   public void onEventInsert(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        rowData.getAfterColumnsList().forEach(column -> System.out.println("增加数据监听:"+column.getName()+":"+column.getValue()));
   }

   /**
     * 删除数据监听
     * @param eventType
     * @param rowData
     */
   @DeleteListenPoint
   public void onEventDelete(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        rowData.getAfterColumnsList().forEach(column -> System.out.println("删除数据监听:"+column.getName()+":"+column.getValue()));
   }

    /**
     * 自定义的监听操作
     * @param eventType
     * @param rowData
     */
   @ListenPoint(destination = "example",
                schema = "shop_content",
                table = {"tb_content","tb_content_category"},
                eventType = {
                        CanalEntry.EventType.UPDATE,
                        CanalEntry.EventType.DELETE,
                        CanalEntry.EventType.INSERT
                })
    public void onEvent(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
       String category_id = getColumnValue(eventType, rowData);
       //调用feign获取该分类下的所有广告集合
       Result<List<Content>> result = contentFeign.findByCategoryId(Long.valueOf(category_id));
       //得到广告数据
       List<Content> contents = result.getData();
       //将信息更新到redis中
       stringRedisTemplate.boundValueOps("content_"+category_id).set(JSON.toJSONString(contents));
   }

    /**
     * 得到修改数据的category_id
     * @param eventType
     * @param rowData
     * @return
     */
   private String getColumnValue(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
       if (eventType== CanalEntry.EventType.DELETE){
           //删除操作,取得之前的数据
           for (CanalEntry.Column column :rowData.getBeforeColumnsList()) {
               if (column.getName().equalsIgnoreCase("category_id")){
                   return column.getValue();
               }
           }
       }else {
           //添加和修改的操作
           for (CanalEntry.Column column :rowData.getAfterColumnsList()) {
               if (column.getName().equalsIgnoreCase("category_id")){
                   return column.getValue();
               }
           }
       }
       return "";
   }
}
