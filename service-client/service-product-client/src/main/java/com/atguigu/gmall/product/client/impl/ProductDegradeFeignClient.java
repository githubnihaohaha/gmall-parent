package com.atguigu.gmall.product.client.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/4,15:44
 */
@Component
public class ProductDegradeFeignClient implements ProductFeignClient {
    /**
     * 查询商品的所有销售属性及属性值
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return null;
    }
    
    /**
     * 根据spuId查询所有对应的sku关系,以特定的Json格式返回
     *
     * @param spuId
     * @return keyValue格式为 "属性值1|属性值2":"1" 的map
     */
    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        return null;
    }
    
    /**
     * 查询平台属性
     *
     * @param skuId 商品id
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {
        return null;
    }
    
    /**
     * 获取商品的分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView 封装了三级分类id及name的对象
     */
    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        return null;
    }
    
    /**
     * 查询skuInfo
     *
     * @param skuId 商品id
     * @return SkuInfo
     */
    @Override
    public SkuInfo getSkuInfoBySkuId(Long skuId) {
        return null;
    }
    
    /**
     * 根据商品id查询对应的海报
     *
     * @param spuId 商品id
     * @return spuId符合条件的海报集合
     */
    @Override
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        return null;
    }
    
    /**
     * 根据具体商品的id获取最新的售价
     *
     * @param skuId 给定了具体销售属性值的商品id
     * @return BigDecimal类型的售价
     */
    @Override
    public BigDecimal getSkuPriceBySkuId(Long skuId) {
        return null;
    }
}
