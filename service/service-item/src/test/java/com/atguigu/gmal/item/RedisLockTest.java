package com.atguigu.gmal.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptExecutor;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: liu-wēi
 * @date: 2022/12/5,23:40
 */
@SpringBootTest
public class RedisLockTest {
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    
    @Test
    public void test01(){
        for (int i = 0; i < 100; i++) {
            new Thread(this::testLock).start();
        }
    }
    
    void testLock() {
        
        String value = UUID.randomUUID().toString().replaceAll("-", "");
        
        boolean success = redisTemplate.opsForValue().setIfAbsent("lock", value, 3, TimeUnit.SECONDS);
        
        if (success) {
            // 获取锁成功操作数据库
            System.out.println(Thread.currentThread().getName()+"获得锁,正在操作数据库;");
            
            // 操作完数据库后,自己释放锁,使用lua脚本优化
            String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                    "then\n" +
                    "    return redis.call(\"del\",KEYS[1])\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end";
//
//            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
//            redisScript.setScriptText(script);
//
//            redisTemplate.execute(redisScript, Arrays.asList("lock"),value);
            
            System.out.println(Thread.currentThread().getName()+"结束业务,释放锁~~~");
            redisTemplate.delete("lock");
            
        } else {
            
            try {
                Thread.sleep(300);
                this.testLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        
        
    }
    
    
}
