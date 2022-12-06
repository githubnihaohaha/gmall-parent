package com.atguigu.gmall.common.cache;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.constant.RedisConst;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author: liu-wēi
 * @date: 2022/12/6,17:02
 */
@Component
@Aspect
public class GmallCacheAspect {
    
    @Autowired
    private RedissonClient redissonClient;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    /**
     * 使用aop+注解实现分布式锁+缓存
     *
     * @param joinPoint 连接点对象
     * @return 所需的redis或DB中的数据
     */
    @Around("@annotation(com.atguigu.gmall.common.cache.GmallCache)") // 确定切入点(要增强功能的类)
    public Object cacheAspectGmall(ProceedingJoinPoint joinPoint) throws Throwable {
        
        // 创建通用的返回对象
        Object obj;
        
        // 获取方法参数信息
        Object[] args = joinPoint.getArgs();
        
        /*
         * 获得从redis中获取数据的key,由注解中传入的前缀 + 参数 + 后缀组成.可以从方法签名中获取需要的注解信息
         * 1.获取方法签名信息
         * 2.从方法签名中获取注解的信息
         * 3.从注解中获取前缀和后缀
         * 4.前缀 + 方法参数 + 后缀 = Key
         * */
        
        // 这个注解最终作用于方法(所以数据一定来源于方法),所以强转为方法的签名信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取注解
        GmallCache annotation = signature.getMethod().getAnnotation(GmallCache.class);
        // 获取前缀/后缀
        String prefix = annotation.prefix();
        String suffix = annotation.suffix();
        // 拼接redis中用于获取和存储数据的key(将参数列表的数组转换为String格式)
        String key = prefix + Arrays.asList(args).toString() + suffix;
        
        // 尝试从缓存中获取所需要的数据
        try {
            obj = cacheHit(key, signature);
            
            if (obj == null) {
                /*
                 * 未命中缓存,从DB中获取数据
                 * 1.生成锁key
                 * 2.尝试获取锁,获取成功访问数据库,没有获取到锁自旋
                 * 3.释放锁
                 * */
                
                String lockKey = prefix + RedisConst.SKULOCK_SUFFIX;
                
                // 获得一个分布式锁对象
                RLock lock = redissonClient.getLock(lockKey);
                // 尝试获取锁
                boolean tryLock = lock.tryLock();
                
                if (tryLock) {
                    /*
                     * 获取锁成功...操作数据库,查詢數據.将查询到的数据数据转换为Json字符串存入Redis中
                     * 1.如果从数据库中查询为空,则向Redis中存入一个临时的空值,避免缓存穿透
                     * 2.数据不为空,存入Redis
                     *
                     * */
                    
                    try {
                        obj = joinPoint.proceed(args);
                        
                        if (obj == null) {
                            
                            /*
                            这里不能使用Obj实例的方式存入,因为向下转型的的前提是这个对象向上转型过,
                            如果使用Obj实例存入,在外部方法获取这个数据时会出现类型转换异常..
                            所以使用反射创建对象实例更加符合逻辑也更为妥当
                            obj = new Object();
                            */
                            
                            
                            Class returnType = signature.getReturnType();
                            // 创建这个方法返回值的实例对象
                            obj = returnType.newInstance();
                            // 将数据转换为Json字符串存入Redis中
                            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(obj),
                                    RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                            return obj;
                        } else {
                            // 将数据转换为Json字符串存入Redis中
                            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(obj),
                                    RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                            return obj;
                        }
                    } finally {
                        // 释放锁
                        lock.unlock();
                    }
                    
                } else {
                    // 没有获得锁,自旋.休眠一段时间后重新尝试获取数据
                    Thread.sleep(300);
                    this.cacheAspectGmall(joinPoint);
                }
                
            } else {
                // 命中缓存,直接返回缓存中的数据
                return obj;
            }
        } catch (Throwable e) {
            // 最终兜底的方法,因为分布式锁和缓存都使用到了Redis,
            // 为防止Redis宕机后接口直接瘫痪,可以在这种情况下访问DB返回数据
            e.printStackTrace();
            return joinPoint.proceed(args);
        }
        
        return null;
    }
    
    /**
     * 从缓存中获取数据
     *
     * @param key       redis中的key
     * @param signature 方法签名
     * @return 通过Json转换为返回值类型的Object
     */
    private Object cacheHit(String key, MethodSignature signature) {
        // 尝试从Redis中获取数据
        String jsonStr = (String) redisTemplate.opsForValue().get(key);
        
        // 获取到的数据不为空,对数据进行转换 **这里的数据必须转换为对应返回值的类型,不能直接使用Obj**
        if (!StringUtils.isEmpty(jsonStr)) {
            // 获取注解所标记方法的返回值类型
            Class returnType = signature.getReturnType();
            // 将Json字符串通过Json工具转换为对应返回值类型的对象并返回
            return JSONObject.parseObject(jsonStr, returnType);
        }
        return null;
    }
}
