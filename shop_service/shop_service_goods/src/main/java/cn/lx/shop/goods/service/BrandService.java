package cn.lx.shop.goods.service;
import cn.lx.shop.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;
import java.util.List;
/****
 * @Author:shenkunlin
 * @Description:Brand业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface BrandService {

    /***
     * Brand多条件分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, int page, int size);

    /***
     * Brand分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(int page, int size);

    /***
     * Brand多条件搜索方法
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /***
     * 删除Brand
     * @param id
     */
    void delete(Integer id);

    /***
     * 修改Brand数据
     * @param brand
     */
    void update(Brand brand);

    /***
     * 新增Brand
     * @param brand
     */
    void add(Brand brand);

    /**
     * 根据ID查询Brand
     * @param id
     * @return
     */
     Brand findById(Integer id);

    /***
     * 查询所有Brand
     * @return
     */
    List<Brand> findAll();


    /***
     * 根据分类id查询该分类所用有的品牌信息
     * @param id
     * @return
     */
    List<Brand> findByCategoryId(Integer id);
}
