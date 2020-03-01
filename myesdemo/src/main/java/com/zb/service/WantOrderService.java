package com.zb.service;

import com.zb.entity.Course;
import com.zb.entity.Page;
import com.zb.entity.WantOrder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WantOrderService {


    @Autowired(required = false)
    private RestHighLevelClient restHighLevelClient;


    public Page<WantOrder> findOrderByPage(Integer index, Integer size, String key, Integer type_level1, Integer type_level2, Integer type_level3, Integer min, Integer max) throws Exception {
        List<WantOrder> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("want_order");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from((index - 1) * size);
        searchSourceBuilder.size(size);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (key != null) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(key, "name", "title");
            multiMatchQueryBuilder.field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        if (type_level1 != null && type_level1 != 0) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("type_level1", type_level1);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if (type_level2 != null && type_level2 != 0) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("type_level2", type_level2);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if (type_level3 != null && type_level3 != 0) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("type_level3", type_level3);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        if (min != null && max != null && min != -1 && max != -1) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("type_num").gte(min).lte(max);
            boolQueryBuilder.filter(rangeQuery);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<div style='color:red'>");
        highlightBuilder.postTags("</div>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        Long totalHits = hits.totalHits;
        for (SearchHit hit : searchHits) {
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String s1 = sourceAsMap.get("type_level1").toString();
            String s2 = sourceAsMap.get("type_level2").toString();
            String s3 = sourceAsMap.get("type_level3").toString();
            String name = sourceAsMap.get("name").toString();
            String title = sourceAsMap.get("title").toString();

            String id_t = sourceAsMap.get("id").toString();
            String ship_addr = sourceAsMap.get("ship_addr").toString();
            String ship_mobile = sourceAsMap.get("ship_mobile").toString();
            String end_time = sourceAsMap.get("end_time").toString();
            String type_num = sourceAsMap.get("type_num").toString();
            String goods_num = sourceAsMap.get("goods_num").toString();
            //
            //获取高亮的结果数据
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightBuilder != null) {
                HighlightField nameField = highlightFields.get("name");
                if (nameField != null) {
                    Text[] nameTxt = nameField.getFragments();
                    StringBuffer nameStr = new StringBuffer();
                    for (Text text : nameTxt) {
                        nameStr.append(text);
                    }
                    name = nameStr.toString();
                }
                HighlightField titleField = highlightFields.get("title");
                if (titleField != null) {
                    Text[] titleTxt = titleField.getFragments();
                    StringBuffer titleStr = new StringBuffer();
                    for (Text text : titleTxt) {
                        titleStr.append(text);
                    }
                    title = titleStr.toString();
                }
            }
            WantOrder order = new WantOrder();
            order.setId(id_t);
            order.setShip_addr(ship_addr);
            order.setShip_mobile(ship_mobile);
            order.setType_level1(s1);
            order.setType_level2(s2);
            order.setType_level3(s3);
            order.setName(name);
            order.setTitle(title);
            order.setEnd_time(end_time);
            order.setType_num(type_num);
            order.setGoods_num(goods_num);
            list.add(order);

        }
        Page<WantOrder> page = new Page<WantOrder>(index, size, totalHits.intValue());
        page.setRows(list);
        return page;
    }
}
