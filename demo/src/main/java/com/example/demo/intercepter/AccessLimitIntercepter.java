package com.example.demo.intercepter;

import com.alibaba.fastjson.JSON;
import com.example.demo.aop.AccessLimit;
import com.example.demo.enums.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 限制访问拦截器
 * @author qzz
 * @date 2024/1/12
 */
@Slf4j
@Component
public class AccessLimitIntercepter implements HandlerInterceptor {
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public AccessLimitIntercepter(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //方法请求拦截    Handler 是否为 HandlerMethod 实例
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;

            // 获取方法
            Method method = hm.getMethod();
            // 是否有AccessLimit注解
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            //获取方法中的AccessLimit注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key=request.getRequestURI();
            //判断是否登录
            if(needLogin){
                //获取登录的session进行判断
                //...
                //获取用户id
                Long userId = 1L;
                key+=userId;
            }
            //从redis中获取用户访问的次数
            Object count = redisTemplate.opsForValue().get(key);
            log.info("count:{}",count);
            if(count == null){
                //第一次访问
                redisTemplate.opsForValue().set(key,1, Long.valueOf(String.valueOf(seconds)), TimeUnit.SECONDS);
                log.info("--------------------------第一次访问--------------------------");
            }else if(count!=null && Integer.valueOf(String.valueOf(count)) < maxCount){
                //次数自增
                redisTemplate.opsForValue().increment(key,1);
                log.info("--------------------------次数自增--------------------------");
            }else{
                //超出访问次数
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        String str = JSON.toJSONString(codeMsg);
        outputStream.write(str.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
