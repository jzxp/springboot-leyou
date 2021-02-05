package com.juzipi.demo.filter;

import com.juzipi.demo.config.FilterProperties;
import com.juzipi.demo.config.JwtProperties;
import com.juzipi.demo.utils.CookieUtils;
import com.juzipi.demo.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    //过滤器类型
    public String filterType() {
        return "pre";
    }

    @Override
    //顺序
    public int filterOrder() {
        return 10;
    }

    @Override
    //返回值
    public boolean shouldFilter() {
        //获取白名单
        List<String> allowPaths = this.filterProperties.getAllowPaths();
        //初始化zull运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取url
        String url = request.getRequestURL().toString();
        //遍历allowPaths（白名单）
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url, allowPath)) {
                return false;
            }
        }
        return true;

    }

    @Override
    //run方法
    public Object run() throws ZuulException {

        //初始化zull运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取cookieName
        String cookieName = this.jwtProperties.getCookieName();
        //获取cookie值
        String token = CookieUtils.getCookieValue(request,cookieName);


        if (StringUtils.isBlank(token)){
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        try {
            JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
