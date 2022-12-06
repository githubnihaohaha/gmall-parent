package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        resultMap.put("price", skuPrice);
        
        // 商品的平台属性
        List<BaseAttrInfo> skuAttrList = productFeignClient.getAttrList(skuId);
        
        // 前端遍历spuSaleAttrList后直接使用skuAttr.attrName进行取值,所以要对他进行封装
        if (!CollectionUtils.isEmpty(skuAttrList)) {
            List<Map<String, String>> mapList = skuAttrList.stream().map(attrInfo -> {
                Map<String, String> map = new HashMap<>();
                map.put("attrName", attrInfo.getAttrName());
                map.put("attrValue", attrInfo.getAttrValueList().get(0).getValueName());
                return map;
            }).collect(Collectors.toList());
            resultMap.put("skuAttrList", mapList);
        }
        
        return resultMap;
    }
}
