package com.juzipi.demo.controller;

import com.juzipi.demo.pojo.Goods;
import com.juzipi.demo.pojo.PageResult;
import com.juzipi.demo.pojo.SearchRequest;
import com.juzipi.demo.pojo.SearchResult;
import com.juzipi.demo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;


    /**
     * 搜索商品
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request){
        SearchResult result = this.searchService.search(request);
        if (result == null || CollectionUtils.isEmpty(result.getItems())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
}
