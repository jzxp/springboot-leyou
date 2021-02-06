package com.juzipi.demo.interceptor;

import com.juzipi.demo.config.JwtProperties;
import com.juzipi.demo.pojo.UserInfo;
import com.juzipi.demo.utils.CookieUtils;
import com.juzipi.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        //解析token，获取用户信息
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        if (StringUtils.isEmpty(userInfo)){
            return false;
        }
        //把userInfo放入线程局部变量
        THREAD_LOCAL.set(userInfo);

        return true;
    }

    //获取userInfo方法
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空线程的局部变量，因为使用的时tomcat的线程池，线程不会结束，也就不会释放线程的局部变量
        THREAD_LOCAL.remove();
    }
}
