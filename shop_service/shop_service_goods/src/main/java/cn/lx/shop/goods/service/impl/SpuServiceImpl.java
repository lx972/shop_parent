package cn.lx.shop.goods.service.impl;
import cn.lx.shop.entity.IdWorker;
import cn.lx.shop.goods.dao.BrandMapper;
import cn.lx.shop.goods.dao.CategoryMapper;
import cn.lx.shop.goods.dao.SkuMapper;
import cn.lx.shop.goods.dao.SpuMapper;
import cn.lx.shop.goods.pojo.*;
import cn.lx.shop.goods.service.SpuService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:shenkunlin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 保存（修改）商品
     * @return
     */
    @Override
    public void saveGoods(Goods goods) {
        //保存spu
        Spu spu = goods.getSpu();
        if (spu.getId()==null){
            //保存
            spu.setId(idWorker.nextId());
            spuMapper.insertSelective(spu);
        }else {
            //修改
            //修改spu
            spuMapper.updateByPrimaryKeySelective(spu);
            //删除所有sku
            Sku sku = new Sku();
            sku.setId(sku.getSpuId());
            skuMapper.delete(sku);
        }

        //查询出品牌相关信息
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        //查询出分类相关信息
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());

        //保存sku
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {
            //构建SKU名称，采用SPU+规格值组装
            if(StringUtils.isEmpty(sku.getSpec())){
                sku.setSpec("{}");
            }
            String name = sku.getName();
            Map<String,String> map = JSON.parseObject(sku.getSpec(), Map.class);
            for (String value : map.values()) {
                name+="  "+value;
            }
            sku.setName(name);
            //添加创建和修改时间
            Date date = new Date();
            sku.setCreateTime(date);
            sku.setUpdateTime(date);
            //设置spu的id
            sku.setSpuId(spu.getId());
            //设置品牌信息
            sku.setBrandName(brand.getName());
            //设置分类信息
            sku.setCategoryId(category.getId());
            sku.setCategoryName(category.getName());
            //设置id
            sku.setId(idWorker.nextId());
            //添加数据库
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * 根据spuid查询商品信息
     * @param id    spuid
     * @return
     */
    @Override
    public Goods findGoodsById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        Goods goods = new Goods(spu, skuList);
        return goods;
    }


    /**
     * 根据spuid审核商品,自动上架
     * @param id    spuid
     * @return
     */
    @Override
    public void audit(Long id) {
        //得到spu信息
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断该商品是否为删除状态
        if (spu.getIsDelete().equalsIgnoreCase("1")){
            //删除状态
            throw new RuntimeException("该商品为删除状态");
        }
        //商品审核通过
        spu.setStatus("1");
        //商品上架
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 根据spuid下架商品
     * @param id    spuid
     * @return
     */
    @Override
    public void pull(Long id) {
        //得到spu信息
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断该商品是否为删除状态
        if (spu.getIsDelete().equalsIgnoreCase("1")){
            //删除状态
            throw new RuntimeException("该商品为删除状态");
        }
        //商品下架
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 根据spuid上架商品
     * @param id    spuid
     * @return
     */
    @Override
    public void put(Long id) {
        //得到spu信息
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断该商品是否为删除状态
        if (spu.getIsDelete().equalsIgnoreCase("1")){
            //删除状态
            throw new RuntimeException("该商品为删除状态");
        }
        //判断该商品是否已通过审核
        if (spu.getStatus().equalsIgnoreCase("0")){
            //未通过审核
            throw new RuntimeException("该商品未通过审核");
        }
        //商品上架架
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 根据spuid批量上架商品
     * @param ids    spuids
     */
    @Override
    public void putMany(Long[] ids) {
        //修改成的值
        Spu spu = new Spu();
        spu.setIsMarketable("1");
        //哪些数据要被修改
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (ids.length>0){
            //id
            criteria.andIn("id", Arrays.asList(ids));
            //未上架的
            criteria.andEqualTo("isMarketable","0");
            //未删除的
            criteria.andEqualTo("isDelete","0");
            //审核通过的
            criteria.andEqualTo("status","1");
        }
        spuMapper.updateByExampleSelective(spu,example);
    }


    /**
     * 根据spuid批量下架商品
     * @param ids    spuids
     */
    @Override
    public void pullMany(Long[] ids) {
        //修改成的值
        Spu spu = new Spu();
        spu.setIsMarketable("0");
        //哪些数据要被修改
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (ids.length>0){
            //id
            criteria.andIn("id", Arrays.asList(ids));
            //上架的
            criteria.andEqualTo("isMarketable","1");
            //未删除的
            criteria.andEqualTo("isDelete","0");
            //审核通过的
            criteria.andEqualTo("status","1");
        }
        spuMapper.updateByExampleSelective(spu,example);
    }

    /**
     * 逻辑删除商品
     * @param id
     */
    @Override
    public void logicDelete(Long id) {
        Spu spu1 = spuMapper.selectByPrimaryKey(id);
        if (spu1.getIsMarketable().equalsIgnoreCase("1")){
            //未下架不能删除
           throw new RuntimeException("未下架不能删除");
        }
        Spu spu = new Spu();
        spu.setId(id);
        spu.setIsDelete("1");
        //设置为下架状态
        spu.setIsMarketable("0");
        //设置为未审核状态
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 还原被逻辑删除的商品
     * @param id
     */
    @Override
    public void restore(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu.getIsDelete().equalsIgnoreCase("0")){
            throw new RuntimeException("该商品未被删除");
        }
        Spu spu1 = new Spu();
        spu1.setId(id);
        spu1.setIsDelete("0");
        //未审核
        spu1.setStatus("0");
        //未上架
        spu1.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu1);
    }


    /**
     * 物理删除商品
     * @param id
     */
    @Override
    public void delete(Long id) {
        Spu spu1 = spuMapper.selectByPrimaryKey(id);
        if (spu1.getIsDelete().equalsIgnoreCase("1")){
            //未逻辑删除不能物理删除
            throw new RuntimeException("未逻辑删除不能物理删除");
        }
        spuMapper.deleteByPrimaryKey(id);
    }
}
