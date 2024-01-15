package com.example.demo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义接口：api接口限流
 * @author qzz
 * @date 2024/1/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    /**
     * 时间间隔（单位秒）
     * @return
     */
    int seconds() default 60;

    /**
     * 最大访问次数
     * @return
     */
    int maxCount() default 60;

    /**
     * 是否需要登录
     * @return
     */
    boolean needLogin() default true;
}
