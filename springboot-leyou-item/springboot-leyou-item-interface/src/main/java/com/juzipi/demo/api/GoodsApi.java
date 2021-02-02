package com.juzipi.demo.api;

import com.juzipi.demo.bo.SpuBo;
import com.juzipi.demo.pojo.PageResult;
import com.juzipi.demo.pojo.Sku;
import com.juzipi.demo.pojo.Spu;
import com.juzipi.demo.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface GoodsApi {

    //创建一个调用方法的接口供feign 远程调用
    /**
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据条件分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */

    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",defaultValue = "true")Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
    );


    /**
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id")Long spuId);


    /**
     * 根据id查询spu
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Spu querySpuById(@PathVariable("id")Long id);
}
