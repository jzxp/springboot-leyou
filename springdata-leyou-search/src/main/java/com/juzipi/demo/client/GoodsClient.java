package com.juzipi.demo.client;

import com.juzipi.demo.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
//继承GoodsApi接口获得里面的方法
public interface GoodsClient extends GoodsApi {

}
