package com.juzipi.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.juzipi.demo.SearchApplication;
import com.juzipi.demo.bo.SpuBo;
import com.juzipi.demo.client.GoodsClient;
import com.juzipi.demo.pojo.Goods;
import com.juzipi.demo.pojo.PageResult;
import com.juzipi.demo.repository.GoodsRepository;
import com.juzipi.demo.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;


    @Test
    public void test(){
        boolean exists = elasticsearchRestTemplate.indexOps(Goods.class).exists();
		//如果已存在索引就删除
		if (exists) {
			this.elasticsearchRestTemplate.indexOps(Goods.class).delete();
		}
        //创建索引
		this.elasticsearchRestTemplate.indexOps(Goods.class).create();
		//创建映射
		Document mapping = elasticsearchRestTemplate.indexOps(Goods.class).createMapping();
		this.elasticsearchRestTemplate.indexOps(Goods.class).putMapping(mapping);

		System.out.println("建议成功");

        Integer page = 1;
        Integer rows = 100;

        do {
        //分页查询spu，获取分页结果集
        PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, true, page, rows);
        //获取当前页的数据
        List<SpuBo> items = result.getItems();
        //处理List<SpuBo> ==> List<Goods>
        List<Goods> goodsList = items.stream().map(spuBo -> {
            try {
                return this.searchService.buildGoods(spuBo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        //执行新增数据的方法
        this.goodsRepository.saveAll(goodsList);

        //获取当前的数据条数，如果是最后一页，没有100条
        rows = items.size();
        page++;
        }while (rows == 100);
    }

}
