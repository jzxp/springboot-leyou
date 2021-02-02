package com.juzipi.demo.controller;

import com.juzipi.demo.service.GoodsHtmlService;
import com.juzipi.demo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//商品详情页显示不出来，RestController换Controller就行了
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsHtmlService goodsHtmlService;


    /**
     * 跳转到商品详情页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id")Long id,Model model){
        //加载所需的数据
        Map<String, Object> map = this.goodsService.loadData(id);
        //放入模型
        model.addAllAttributes(map);

        this.goodsHtmlService.createHtml(id);
        return "item";
    }
}
