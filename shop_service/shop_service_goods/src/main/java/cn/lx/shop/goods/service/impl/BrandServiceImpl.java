package cn.lx.shop.goods.service.impl;

import cn.lx.shop.goods.dao.BrandMapper;
import cn.lx.shop.goods.pojo.Brand;
import cn.lx.shop.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * cn.lx.shop.goods.service.impl
 *
 * @Author Administrator
 * @date 10:53
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Brand> FindAll() {
        return brandMapper.selectAll();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Brand FindById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加品牌数据
     *
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    /**
     * 修改品牌数据
     *
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 删除品牌数据
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 条件查询品牌数据
     *
     * @param brand
     * @return
     */
    @Override
    public List<Brand> FindListByBrand(Brand brand) {
        Example example = CreateExample(brand);
        return brandMapper.selectByExample(example);
    }

    /**
     * 分页查询品牌数据
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> FindPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Brand>(brandMapper.selectAll());
    }

    /**
     * 条件分页查询品牌数据
     *
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> FindPage(Brand brand, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = CreateExample(brand);
        return new PageInfo<Brand>(brandMapper.selectByExample(example));
    }


    /**
     * 封装查询条件
     *
     * @param brand
     * @return
     */
    public Example CreateExample(Brand brand) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand != null) {
            // 品牌名称
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            // 品牌图片地址
            if (!StringUtils.isEmpty(brand.getImage())) {
                criteria.andLike("image", "%" + brand.getImage() + "%");
            }
            // 品牌的首字母
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
            // 品牌id
            if (!StringUtils.isEmpty(brand.getId())) {
                criteria.andEqualTo("id", brand.getId());
            }
            // 排序
            if (!StringUtils.isEmpty(brand.getSeq())) {
                criteria.andEqualTo("seq", brand.getSeq());
            }
        }
        return example;
    }
}
