package com.juzipi.demo.controller;


import com.juzipi.demo.pojo.Cart;
import com.juzipi.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;


    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts() {
        List<Cart> carts = this.cartService.queryCarts();
        if (CollectionUtils.isEmpty(carts)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }


    /**
     * 修改商品数量
     * @param cart
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.cartService.updateNum(cart);
        return ResponseEntity.noContent().build();
    }
}
