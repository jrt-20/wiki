package com.futureport.wiki.config;

import com.futureport.wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**").excludePathPatterns(
                "/test/**",
                "/user/login",
                "/category/all",
                "/ebook/list",
                "/doc/all/*",
                "/doc/vote/**",
                "/ebook-snapshot/**",
                "/doc/find-content/**");
    }
}
