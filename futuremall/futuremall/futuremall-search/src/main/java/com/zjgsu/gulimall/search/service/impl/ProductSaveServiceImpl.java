package com.zjgsu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjgsu.common.to.es.SkuEsModel;
import com.zjgsu.gulimall.search.config.GulimallElasticSearchConfig;
import com.zjgsu.gulimall.search.constant.EsConstant;
import com.zjgsu.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {

        //保存到es
        //1、给es中建立索引。product，建立好映射关系

        //2、给es中保存这些数据
        //BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            //1、构造保存请求
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //TODO 如果批量错误，可以在这里加一个处理错误
        boolean b = bulk.hasFailures();
        List<String> finish = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("商品上架完成：{}，返回数据：{}",finish,bulk.toString());

        return b;
    }
}
