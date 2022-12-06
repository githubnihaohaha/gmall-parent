package com.atguigu.gmall.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用该注解标记的方法表示优先从缓存中获取数据
 *
 * @author: liu-wēi
 * @date: 2022/12/6,16:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GmallCache {
    /**
     * redis 中的缓存前缀,例如 user:
     */
    String prefix() default "cache:";
    
    /**
     * redis 中的缓存后缀,例如 userId:xx:info
     */
    String suffix() default ":info";
}
