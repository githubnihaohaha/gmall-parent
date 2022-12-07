package com.atguigu.gmall.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: liu-wēi
 * @date: 7 Dec 2022,21:27
 */
@Configuration
public class ThreadPoolConfig {
    
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        
        return new ThreadPoolExecutor(50,
                50,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000));
    }
}
