package com.juzipi.demo.repository;

import com.juzipi.demo.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

}
