package com.community.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 用于自定义拦截器的注解
// 表明可以被声明在方法之上，表示要拦截
@Target(ElementType.METHOD)
// 声明生效时间
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
