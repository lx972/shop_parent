package cn.lx.shop.goods.service;
import cn.lx.shop.goods.pojo.Goods;
import cn.lx.shop.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;
import java.util.List;
/****
 * @Author:shenkunlin
 * @Description:Spu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpuService {

    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
     Spu findById(String id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();

    /***
     * 保存商品
     * @return
     */
    void saveGoods(Goods goods);

    /***
     * 根据spuid查询商品信息
     * @param id    spuid
     * @return
     */
    Goods findGoodsById(Long id);


    /***
     * 根据spuid审核商品,自动上架
     * @param id    spuid
     * @return
     */
    void audit(Long id);


    /***
     * 根据spuid下架商品
     * @param id    spuid
     * @return
     */
    void pull(Long id);


    /***
     * 根据spuid上架商品
     * @param id    spuid
     * @return
     */
    void put(Long id);

    /***
     * 根据spuid批量上架商品
     * @param ids    spuids
     * @return
     */
    void putMany(Long[] ids);

    /***
     * 根据spuid批量下架商品
     * @param ids    spuids
     * @return
     */
    void pullMany(Long[] ids);

    /***
     * 逻辑删除商品
     * @param id
     * @return
     */
    void logicDelete(Long id);

    /***
     * 还原被逻辑删除的商品
     * @param id
     * @return
     */
    void restore(Long id);

    /***
     * 物理删除商品
     * @param id
     * @return
     */
    void delete(Long id);
}
