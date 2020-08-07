package cn.lx.shop.goods.controller;

import cn.lx.shop.entity.PageResult;
import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.goods.pojo.Brand;
import cn.lx.shop.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * cn.lx.shop.goods.controller
 *
 * @Author Administrator
 * @date 10:57
 */
@RestController
@RequestMapping(value = "/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @GetMapping
    public Result<List<Brand>> getAll() {
        List<Brand> all = brandService.FindAll();
        return new Result<List<Brand>>(true, StatusCode.OK, "查询成功", all);
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result<Brand> getOneById(@PathVariable("id") Integer id) {
        Brand brand = brandService.FindById(id);
        return new Result<Brand>(true, StatusCode.OK, "查询成功", brand);
    }

    /**
     * 添加品牌数据
     *
     * @param brand
     * @return
     */
    @PostMapping
    public Result<Brand> addBrand(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result<Brand>(true, StatusCode.OK, "添加成功");
    }

    /**
     * 修改品牌数据
     *
     * @param brand
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result<Brand> updateBrand(@PathVariable("id")Integer id,@RequestBody Brand brand) {
        brand.setId(id);
        brandService.update(brand);
        return new Result<Brand>(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除品牌数据
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result<Brand> deleteBrand(@PathVariable("id")Integer id) {
        brandService.delete(id);
        return new Result<Brand>(true, StatusCode.OK, "删除成功");
    }

    /**
     * 根据条件查询品牌
     *
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Brand>> getListByBrand(@RequestBody(required = false) Brand brand) {
        List<Brand> list = brandService.FindListByBrand(brand);
        return new Result<List<Brand>>(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 分页查询品牌数据
     *
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public PageResult<Brand> FindPage(@PathVariable("page")Integer page,@PathVariable("size")Integer size) {
        PageInfo<Brand> brandPageInfo = brandService.FindPage(page, size);
        return new PageResult<Brand>(brandPageInfo.getTotal(),brandPageInfo.getList());
    }

    /**
     * 分页搜索品牌数据
     *
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public PageResult<Brand> FindPage(@RequestBody(required = false) Brand brand,@PathVariable("page")Integer page,@PathVariable("size")Integer size) {
        PageInfo<Brand> brandPageInfo = brandService.FindPage(brand,page, size);
        return new PageResult<Brand>(brandPageInfo.getTotal(),brandPageInfo.getList());
    }
}
