package com.juzipi.demo.service;

import com.juzipi.demo.client.UserClient;
import com.juzipi.demo.config.JwtProperties;
import com.juzipi.demo.pojo.User;
import com.juzipi.demo.pojo.UserInfo;
import com.juzipi.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class AuthService {


    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录授权
     *
     * @param username
     * @param password
     * @return
     */
    public String accredit(String username, String password) {
        //根据用户名和密码查询
        User user = this.userClient.queryUser(username, password);
        //判断是否为空
        if (StringUtils.isEmpty(user)){
            return null;
        }

        //通过jwtUtils 生成 jwt类的token
        try {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());

           return JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
