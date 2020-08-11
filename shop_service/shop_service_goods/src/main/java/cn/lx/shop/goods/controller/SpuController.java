package cn.lx.shop.goods.controller;

import cn.lx.shop.entity.PageResult;
import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.goods.pojo.Goods;
import cn.lx.shop.goods.pojo.Spu;
import cn.lx.shop.goods.service.SpuService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/
@Api(value = "SpuController")
@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    /***
     * Spu分页条件搜索实现
     * @param spu
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "Spu条件分页查询",notes = "分页条件查询Spu方法详情",tags = {"SpuController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/search/{page}/{size}" )
    public PageResult<Spu> findPage(@RequestBody(required = false) @ApiParam(name = "Spu对象",value = "传入JSON数据",required = false) Spu spu, @PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /***
     * Spu分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @ApiOperation(value = "Spu分页查询",notes = "分页查询Spu方法详情",tags = {"SpuController"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "path", name = "size", value = "每页显示条数", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/search/{page}/{size}" )
    public PageResult<Spu> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /***
     * 多条件搜索品牌数据
     * @param spu
     * @return
     */
    @ApiOperation(value = "Spu条件查询",notes = "条件查询Spu方法详情",tags = {"SpuController"})
    @PostMapping(value = "/search" )
    public Result<List<Spu>> findList(@RequestBody(required = false) @ApiParam(name = "Spu对象",value = "传入JSON数据",required = false) Spu spu){
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Spu根据ID删除",notes = "根据ID删除Spu方法详情",tags = {"SpuController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Spu数据
     * @param spu
     * @param id
     * @return
     */
    @ApiOperation(value = "Spu根据ID修改",notes = "根据ID修改Spu方法详情",tags = {"SpuController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "Spu对象",value = "传入JSON数据",required = false) Spu spu, @PathVariable Long id){
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Spu数据
     * @param spu
     * @return
     */
    @ApiOperation(value = "Spu添加",notes = "添加Spu方法详情",tags = {"SpuController"})
    @PostMapping
    public Result add(@RequestBody  @ApiParam(name = "Spu对象",value = "传入JSON数据",required = true) Spu spu){
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Spu根据ID查询",notes = "根据ID查询Spu方法详情",tags = {"SpuController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable String id){
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true,StatusCode.OK,"查询成功",spu);
    }

    /***
     * 查询Spu全部数据
     * @return
     */
    @ApiOperation(value = "查询所有Spu",notes = "查询所Spu有方法详情",tags = {"SpuController"})
    @GetMapping
    public Result<List<Spu>> findAll(){
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK,"查询成功",list) ;
    }


    /***
     * 添加(修改）商品
     * @param goods
     * @return
     */
    @PostMapping(value = "/save")
    public Result saveGoods(@RequestBody Goods goods){
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK,"保存商品成功") ;
    }


    /***
     * 根据spuid查询商品信息
     * @param id
     * @return
     */
    @GetMapping(value = "/goods/{id}")
    public Result findGoodsById(@PathVariable("id") Long id){
        spuService.findGoodsById(id);
        return new Result(true, StatusCode.OK,"根据spuid查询商品信息成功") ;
    }

    /***
     * 根据spuid审核商品,自动上架
     * @param id
     * @return
     */
    @PutMapping(value = "/audit/{id}")
    public Result audit(@PathVariable("id") Long id){
        spuService.audit(id);
        return new Result(true, StatusCode.OK,"根据spuid审核，自动上架商品成功") ;
    }

    /***
     * 根据spuid下架商品
     * @param id
     * @return
     */
    @PutMapping(value = "/pull/{id}")
    public Result pull(@PathVariable("id") Long id){
        spuService.pull(id);
        return new Result(true, StatusCode.OK,"根据spuid下架商品成功") ;
    }

    /***
     * 根据spuid上架商品
     * @param id
     * @return
     */
    @PutMapping(value = "/put/{id}")
    public Result put(@PathVariable("id") Long id){
        spuService.put(id);
        return new Result(true, StatusCode.OK,"根据spuid上架商品成功") ;
    }

    /***
     * 根据spuid批量上架商品
     * @param ids
     * @return
     */
    @PutMapping(value = "/put/many")
    public Result putMany(@RequestBody Long[] ids){
        spuService.putMany(ids);
        return new Result(true, StatusCode.OK,"根据spuids批量上架商品成功") ;
    }

    /***
     * 根据spuid批量下架商品
     * @param ids
     * @return
     */
    @PutMapping(value = "/pull/many")
    public Result pullMany(@RequestBody Long[] ids){
        spuService.pullMany(ids);
        return new Result(true, StatusCode.OK,"根据spuid批量下架商品成功") ;
    }

    /***
     * 逻辑删除商品
     * @param id
     * @return
     */
    @DeleteMapping(value = "/logic/delete/{id}")
    public Result logicDelete(@PathVariable("id") Long id){
        spuService.logicDelete(id);
        return new Result(true, StatusCode.OK,"逻辑删除商品成功") ;
    }

    /***
     * 还原被逻辑删除的商品
     * @param id
     * @return
     */
    @PutMapping(value = "/restore/{id}")
    public Result restore(@PathVariable("id") Long id){
        spuService.restore(id);
        return new Result(true, StatusCode.OK,"还原被逻辑删除的商品成功") ;
    }
}
