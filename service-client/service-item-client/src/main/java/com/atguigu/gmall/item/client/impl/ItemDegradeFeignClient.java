package com.atguigu.gmall.item.client.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.client.ItemFeignClient;

/**
 * @author: liu-wēi
 * @date: 2022/12/5,20:25
 */
public class ItemDegradeFeignClient implements ItemFeignClient {
    /**
     * 查询商品所有信息
     *
     * @param skuId 库存单元id
     * @return
     */
    @Override
    public Result getProductInfoBySkuId(Long skuId) {
        return Result.fail();
    }
}
