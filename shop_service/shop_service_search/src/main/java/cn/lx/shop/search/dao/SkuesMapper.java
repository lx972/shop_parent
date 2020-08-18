package cn.lx.shop.search.dao;

import cn.lx.shop.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * cn.lx.shop.search.dao
 *
 * @Author Administrator
 * @date 12:15
 */
@Repository
public interface SkuesMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
