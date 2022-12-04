package com.atguigu.gmall.item.service;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/4,15:53
 */
@Repository
public interface ItemService {
    
    /**
     * 查询商品所有信息
     *
     * @param skuId 库存单元id
     * @return
     */
    Map<String,Object> getProductInfoBySkuId(Long skuId);
}
