package com.atguigu.gmall.weball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: liu-wēi
 * @date: 2022/12/5,20:39
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.atguigu.gmall"})
@EnableFeignClients(basePackages = "com.atguigu.gmall")
public class WebAllApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllApplication.class, args);
    }
}
