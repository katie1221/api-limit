package com.example.demo.controller;

import com.example.demo.aop.AccessLimit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拦截器注册
 * @author qzz
 * @date 2024/1/11
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping("test/api/limit")
    public String testApiLimit(){
        return "test api limit";
    }
}
