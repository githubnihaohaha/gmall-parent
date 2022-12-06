package com.atguigu.gmall.product;

import com.atguigu.gmall.common.constant.RedisConst;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: liu-wēi
 * @date: 2022/11/28,12:09
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu.gmall")
@EnableDiscoveryClient
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
public class ServiceProductApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class, args);
    }
    
    /**
     * 用于初始化布隆过滤器
     */
    @Autowired
    private RedissonClient redissonClient;
    
    @Override
    public void run(String... args) throws Exception {
        
        long dataSize = 100000L;
        double missProbability = 0.01;
        
        // 获取布隆过滤器
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
        /*
          初始化数据:
          dataSize:预估数据量
          missProbability:误判率
         */
        bloomFilter.tryInit(dataSize, missProbability);
        
    }
}
