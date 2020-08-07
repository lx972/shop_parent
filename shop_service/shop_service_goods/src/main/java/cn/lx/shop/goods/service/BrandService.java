package cn.lx.shop.goods.service;

import cn.lx.shop.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * cn.lx.shop.goods.service
 *
 * @Author Administrator
 * @date 10:52
 */
public interface BrandService {

    /**
     * 查询所有
     *
     * @return
     */
    List<Brand> FindAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Brand FindById(Integer id);


    /**
     * 添加品牌数据
     *
     * @param brand
     */
    void add(Brand brand);


    /**
     * 修改品牌数据
     *
     * @param brand
     */
    void update(Brand brand);

    /**
     * 删除品牌数据
     *
     * @param id
     * @return
     */
    void delete(Integer id);

    /**
     * 条件查询品牌数据
     *
     * @param brand
     * @return
     */
    List<Brand> FindListByBrand(Brand brand);


    /**
     * 分页查询品牌数据
     *
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> FindPage(Integer page, Integer size);


    /**
     * 条件分页查询品牌数据
     *
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> FindPage(Brand brand,Integer page, Integer size);
}
