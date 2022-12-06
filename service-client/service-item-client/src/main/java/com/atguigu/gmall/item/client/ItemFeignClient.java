package com.atguigu.gmall.item.client;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.client.impl.ItemDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/5,20:24
 */
@FeignClient(value = "service-item",fallback = ItemDegradeFeignClient.class)
public interface ItemFeignClient {
    
    /**
     * 查询商品所有信息
     *
     * @param skuId 库存单元id
     * @return
     */
    @GetMapping("/api/item/{skuId}")
    public Result getProductInfoBySkuId(@PathVariable Long skuId);
}
