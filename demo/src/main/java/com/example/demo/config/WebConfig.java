package com.example.demo.config;

import com.example.demo.intercepter.AccessLimitIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author qzz
 * @date 2024/1/12
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccessLimitIntercepter accessLimitIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(accessLimitIntercepter);
    }
}
