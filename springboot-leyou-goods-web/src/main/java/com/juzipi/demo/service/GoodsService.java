package com.juzipi.demo.service;

import com.juzipi.demo.client.BrandClient;
import com.juzipi.demo.client.CategoryClient;
import com.juzipi.demo.client.GoodsClient;
import com.juzipi.demo.client.SpecificationClient;
import com.juzipi.demo.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;


    public Map<String,Object> loadData(Long spuId){
        HashMap<String, Object> model = new HashMap<>();
        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);
        //查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        //查询分类，Map<String, Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);        //初始化
        //初始化一个分类的map
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }


        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        //查询规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        //初始化特殊规格参数的map
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });

        //封装spu
        model.put("spu",spu);
        //封装spuDetail
        model.put("spuDetail",spuDetail);
        //封装sku集合
        model.put("categories",categories);
        //分类
        model.put("brand",brand);
        //品牌
        model.put("skus",skus);
        //规格参数
        model.put("groups",groups);
        //查询特殊规格参数
        model.put("paramMap",paramMap);

        return model;
    }
}
