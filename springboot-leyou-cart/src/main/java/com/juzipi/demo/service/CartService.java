package com.juzipi.demo.service;

import com.juzipi.demo.client.GoodsClient;
import com.juzipi.demo.interceptor.LoginInterceptor;
import com.juzipi.demo.pojo.Cart;
import com.juzipi.demo.pojo.Sku;
import com.juzipi.demo.pojo.UserInfo;
import com.juzipi.demo.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";


    /**
     * 添加购物车
     * @param cart
     * @return
     */
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //查询购物车记录，
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //判断当前的商品是否在购物车中
        if (hashOperations.hasKey(key)) {
            //在更新数据
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum()+num);

        }else {
            //不在新增购物车
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
        }
        hashOperations.put(key,JsonUtils.serialize(cart));
        //在更新数量

        //不在就新增购物车

    }


    /**
     * 查询购物车列表
     * @return
     */
    public List<Cart> queryCarts() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //判断用户购物车是否有记录
        if (!this.stringRedisTemplate.hasKey(KEY_PREFIX+userInfo.getId())){
            return null;
        }
        //获取用户的购物车信息
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        //获取购物车Map中所有Cart值集合
        List<Object> cartsJson = hashOperations.values();
        //如果购物车集合为空返回null
        if (CollectionUtils.isEmpty(cartsJson)){
            return null;
        }

        //把List<Object> 集合转化为 List<Cart>
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class))
                .collect(Collectors.toList());


    }


    /**
     * 修改商品数量
     * @param cart
     */
    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户购物车是否有记录
        if (!this.stringRedisTemplate.hasKey(KEY_PREFIX+userInfo.getId())){
            return ;
        }

        Integer num = cart.getNum();

        //获取用户的购物车信息
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson,Cart.class);
        cart.setNum(num);

        hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }
}
