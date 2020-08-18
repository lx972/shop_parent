package cn.lx.shop.search.service.impl;

import cn.lx.shop.entity.Result;
import cn.lx.shop.goods.feign.SkuFeign;
import cn.lx.shop.goods.pojo.Sku;
import cn.lx.shop.search.dao.SkuesMapper;
import cn.lx.shop.search.pojo.SkuInfo;
import cn.lx.shop.search.service.SkuesService;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * cn.lx.shop.search.service.impl
 *
 * @Author Administrator
 * @date 12:11
 */
@Service
public class SkuesServiceImpl implements SkuesService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuesMapper skuesMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 导入sku数据到elasticsearch
     */
    @Override
    public void importSku() {
        Result<List<Sku>> result = skuFeign.findByStatus("1");
        //将sku转化为skuinfo
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(result.getData()), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map<String, Object> map = JSON.parseObject(skuInfo.getSpec());
            skuInfo.setSpecMap(map);
        }
        skuesMapper.saveAll(skuInfos);
    }

    /**
     * 关键字搜索
     *
     * @param searchMap
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {
        //创建查询对象的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //查询结果关键词高亮显示
        //指定高亮域，要高亮显示的字段
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        //高亮显示的格式
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder()
                .preTags("<em style=\"color:red\">")
                .postTags(("</em>")));
        //多条件查询
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(searchMap, nativeSearchQueryBuilder);

        //设置分组
        setGroupList(nativeSearchQueryBuilder);

        //构建过滤查询
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        //构建关键字对象
        NativeSearchQuery build = nativeSearchQueryBuilder.build();

        //进行查询
        //AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(build, SkuInfo.class);
        //加入了高亮的处理的查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(build, SkuInfo.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                //获取所有非高亮数据
                SearchHits hits = searchResponse.getHits();
                //声明一个list用来保存加了高亮的结果
                List<T> list=new ArrayList<>();
                //遍历
                for (SearchHit hit : hits) {
                    //得到一条非高亮的数据json
                    String sourceAsString = hit.getSourceAsString();
                    //将其转化为对象格式
                    SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
                    //获取高亮的数据(需要指定域）
                    HighlightField highlightField = hit.getHighlightFields().get("name");
                    if (highlightField!=null&&highlightField.getFragments()!=null){
                        //高亮数据读取
                        Text[] fragments = highlightField.getFragments();
                        StringBuffer stringBuffer=new StringBuffer();
                        for (Text fragment : fragments) {
                            stringBuffer.append(fragment.toString());
                            System.out.println(fragment.toString());
                        }
                        skuInfo.setName(stringBuffer.toString());
                    }
                    list.add((T)skuInfo);
                }
                return new AggregatedPageImpl<T>(list
                        ,pageable   //分页
                        ,hits.getTotalHits()    //总记录数
                        ,searchResponse.getAggregations()   //聚合结果
                        ,searchResponse.getScrollId()); //排序
            }
        });
        //返回结果
        Map<String, Object> map = new HashMap<>(16);
        map.put("rows", skuInfos.getContent());
        map.put("total", skuInfos.getTotalElements());
        map.put("totalPages", skuInfos.getTotalPages());

        //条件中存在分类的时候，就不显示分类名的聚合结果
        if (searchMap.get("categoryName") == null) {
            //处理分类名的聚合结果
            List<String> categoryList = handleAggregation(skuInfos, "categoryGroup");
            map.put("categoryList", categoryList);
        }

        //条件中存在品牌的时候，就不显示品牌名的聚合结果
        if (searchMap.get("brandName") == null) {
            //处理品牌名的聚合结果
            List<String> brandList = handleAggregation(skuInfos, "brandGroup");
            map.put("brandList", brandList);
        }


        //处理规格spec的聚合结果
        List<String> specList = handleAggregation(skuInfos, "specGroup");
        //将规格信息转化为map(前端所需要的格式）
        Map<String, Set<String>> specMap = handleSpec(specList);
        map.put("specMap", specMap);

        return map;
    }

    /**
     * 设置分组
     * @param nativeSearchQueryBuilder
     */
    private void setGroupList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //添加聚合条件(分类名），size表示统计多少条
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("categoryGroup").field("categoryName").size(50));
        //添加聚合条件(品牌名）
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("brandGroup").field("brandName").size(50));
        //添加聚合条件(spec）
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("specGroup").field("spec.keyword").size(50));
    }

    /**
     * 设置多条件查询的参数
     * @param searchMap
     * @param nativeSearchQueryBuilder
     * @return
     */
    private BoolQueryBuilder getBoolQueryBuilder(Map<String, String> searchMap, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //多条件查询的对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (null != searchMap && searchMap.size() > 0) {
            String keywords = (String) searchMap.get("keywords");
            if (!StringUtils.isEmpty(keywords)) {
                //设置关键字查询的条件matchQuery会先分词，然后在查询，termQuery直接查询
                boolQueryBuilder.must(QueryBuilders.queryStringQuery(keywords).field("name"));
            }
            String brandName = (String) searchMap.get("brandName");
            if (!StringUtils.isEmpty(brandName)) {
                //设置按品牌名的过滤查询matchQuery会先分词，然后在查询，termQuery直接查询
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", brandName));
            }
            String categoryName = (String) searchMap.get("categoryName");
            if (!StringUtils.isEmpty(categoryName)) {
                //设置关按分类名的过滤查询matchQuery会先分词，然后在查询，termQuery直接查询
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", categoryName));
            }
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                if (entry.getKey().startsWith("spec_")) {
                    //和前端约定，以spec_开头的是规格,规格域名的拼接 specMap.口味   specMap.尺码
                    String name = "specMap." + entry.getKey().substring(5) + ".keyword";
                    System.out.println(name);
                    System.out.println(entry.getValue());
                    //设置按规格名的过滤查询matchQuery会先分词，然后在查询，termQuery直接查询
                    boolQueryBuilder.must(QueryBuilders.termQuery(name, entry.getValue()));
                }
            }
            String price = (String) searchMap.get("price");
            if (!StringUtils.isEmpty(price)) {
                //格式为300-500
                String[] split = price.split("-");
                //设置价格区间查询
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").from(split[0], true).to(split[1], true));
            }
            //分页查询参数
            setPageCondition(searchMap, nativeSearchQueryBuilder);
            //排序
            //要排序的类型desc  asc
            String sortRule = (String) searchMap.get("sortRule");
            //要排序的字段
            String sortField = (String) searchMap.get("sortField");
            if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
                //设置排序
                nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equalsIgnoreCase("desc") ? SortOrder.DESC : SortOrder.ASC));
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 封装分页查询
     *
     * @param searchMap
     * @param nativeSearchQueryBuilder
     */
    private void setPageCondition(Map<String, String> searchMap, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //第几页
        Integer pageNum = 1;
        //每页的记录数
        Integer pageSize = 10;
        if (searchMap.containsKey("pageNum")){
            //第几页
            pageNum = Integer.valueOf(searchMap.get("pageNum"));
            if (pageNum<1){
                pageNum=1;
            }
        }
       if (searchMap.containsKey("pageSize")){
           //每页的记录数
           pageSize = Integer.valueOf(searchMap.get("pageSize"));
           if (pageSize<1){
               pageSize=10;
           }
        }
        //分页查
        System.out.println(pageNum);
        System.out.println(pageSize);
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum-1, pageSize));
    }

    /**
     * 将规格信息转化为map(前端所需要的格式)
     *
     * @param specList
     * @return
     */
    private Map<String, Set<String>> handleSpec(List<String> specList) {
        //声明一个Map<String,Set<String>>用来保存不重复的规格，set集合去重
        Map<String, Set<String>> specMap = new HashMap<>(16);
        //遍历
        for (String spec : specList) {
            //将其转化为map
            Map<String, String> specInfoMap = JSON.parseObject(spec, Map.class);
            //遍历map
            for (Map.Entry<String, String> entry : specInfoMap.entrySet()) {
                //声明一个set集合，用来保存每条数据中的规格信息，防止重复
                Set<String> specSet = null;
                //首先要进行判断，specMap中是否已经存在该种类型的规格
                if (specMap.containsKey(entry.getKey())) {
                    //存在就取出来
                    specSet = specMap.get(entry.getKey());
                } else {
                    //不存在就新创建一个
                    specSet = new HashSet<>();
                }
                //将值存入specSet中
                specSet.add(entry.getValue());
                //key作为specMap的key
                specMap.put(entry.getKey(), specSet);
            }
        }
        return specMap;
    }

    /**
     * 处理聚合的结果
     *
     * @param skuInfos
     * @param terms
     * @return
     */
    private List<String> handleAggregation(AggregatedPage<SkuInfo> skuInfos, String terms) {
        //获取指定域的数据（就是分组数据）
        StringTerms stringTerms = skuInfos.getAggregations().get(terms);
        //对分组的结果进行处理
        //声明list集合用来保存分组类型的数据
        List<String> list = new ArrayList<>();
        //得到分组数据的集合
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            list.add(bucket.getKeyAsString());
        }
        return list;
    }


}
