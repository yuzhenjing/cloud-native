package com.cloud.kxlist.brand;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandSearchService {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public String search() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withIndices("kx_brand");

        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        builder.must(QueryBuilders.termQuery("location", "日本"));
        builder.should(QueryBuilders.termQuery("categoryIDs", 1));


        queryBuilder.withQuery(builder);

        AggregatedPage<BrandContent> brandContents = elasticsearchTemplate.queryForPage(queryBuilder.build(), BrandContent.class);
        List<BrandContent> collect = brandContents.get().collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect));
        return "完成";
    }


}
