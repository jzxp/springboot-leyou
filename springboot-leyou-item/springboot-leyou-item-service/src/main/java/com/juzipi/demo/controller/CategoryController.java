package com.juzipi.demo.controller;

import com.juzipi.demo.pojo.Category;
import com.juzipi.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点的id查询子节点
     * @param pid
     * @return
     */

    @GetMapping("list")
    //RESTful写法                                                                         默认查询1级类目
    public ResponseEntity<List<Category>> queryCategoriesById(@RequestParam(value = "pid",defaultValue = "0")Long pid){
            if (pid == null || pid < 0){
                //400 参数不合法
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                //优化
                //badRequest 就是把status(HttpStatus.BAD_REQUEST)封装了起来
                return ResponseEntity.badRequest().build();
            }
            List<Category> categories = this.categoryService.queryCategoriesById(pid);
            if (CollectionUtils.isEmpty(categories)){
                //404 资源服务器未找到
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                return ResponseEntity.notFound().build();
            }
            //200  查询成功
            return ResponseEntity.ok(categories);
        }
        //500 服务器内部错误


    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = this.categoryService.queryNameByIds(ids);
        if (CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }
}
