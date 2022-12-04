package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/4,16:12
 */
@Service
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ProductFeignClient productFeignClient;
    
    
    /**
     * 查询商品所有信息
     *
     * @param skuId 库存单元id
     * @return
     */
    @Override
    public Map<String, Object> getProductInfoBySkuId(Long skuId) {
        // 封装数据
        HashMap<String, Object> resultMap = new HashMap<>();
        
        // skuInfo
        SkuInfo skuInfo = productFeignClient.getSkuInfoBySkuId(skuId);
        resultMap.put("skuInfo", skuInfo);
        
        if (skuInfo != null) {
            // categoryView
            BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
            resultMap.put("categoryView", categoryView);
            
            // spuSaleAttrList
            List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
            resultMap.put("spuSaleAttrList", spuSaleAttrList);
            
            // 每个spu对应的多组sku数据
            Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
            String valuesSkuJson = JSON.toJSONString(skuValueIdsMap);
            resultMap.put("valuesSkuJson", valuesSkuJson);
            
            // spuPosterList
            List<SpuPoster> spuPosterList = productFeignClient.findSpuPosterBySpuId(skuInfo.getSpuId());
            resultMap.put("spuPosterList", spuPosterList);
            
        }
        // 价格
        BigDecimal skuPrice = productFeignClient.getSkuPriceBySkuId(skuId);
        resultMap.put("skuPrice", skuPrice);
        
        // 商品的平台属性
        List<BaseAttrInfo> attrList = productFeignClient.getAttrList(skuId);
        resultMap.put("skuAttrList", attrList);
        
        return resultMap;
    }
}
