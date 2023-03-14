package com.agarthasf.epipre.config;

import com.agarthasf.epipre.interceptor.LogInterceptor;
import com.agarthasf.epipre.interceptor.LoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Component
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LogInterceptor logInterceptor;

    @Resource
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(logInterceptor);
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login", "/hello", "/logout");

        registry.addInterceptor(loginInterceptor);
    }



}
