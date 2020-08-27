package cn.lx.shop.goods.dao;
import cn.lx.shop.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:shenkunlin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {

    /**
     * 下单后更新商品库存,增加销量
     * @param skuId
     * @param num
     * @return
     */
    @Update("UPDATE tb_sku SET num=num-#{num},sale_num=sale_num+#{num} WHERE num>=#{num} AND id=#{skuId}")
    int decrCount(@Param("skuId") String skuId,@Param("num") String num);
}
