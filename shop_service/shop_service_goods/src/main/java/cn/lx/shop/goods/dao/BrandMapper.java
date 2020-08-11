package cn.lx.shop.goods.dao;
import cn.lx.shop.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:Brand的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface BrandMapper extends Mapper<Brand> {

    /***
     * 根据分类id查询该分类所用有的品牌信息
     * @param id
     * @return
     */
    @Select("SELECT t1.* FROM tb_brand t1, tb_category_brand t2 WHERE t1.id=t2.brand_id AND t2.category_id=#{id}")
    List<Brand> findByCategoryId(Integer id);
}
