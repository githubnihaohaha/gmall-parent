package com.atguigu.gmall.product.client;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.impl.ProductDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/4,15:42
 */
@FeignClient(value = "service-product", fallback = ProductDegradeFeignClient.class)
public interface ProductFeignClient {
    
    /**
     * 查询商品的所有销售属性及属性值
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @GetMapping("/api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId,
                                                          @PathVariable Long spuId);
    
    /**
     * 根据spuId查询所有对应的sku关系,以特定的Json格式返回
     *
     * @param spuId
     * @return keyValue格式为 "属性值1|属性值2":"1" 的map
     */
    @GetMapping("/api/product/inner/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable Long spuId);
    
    /**
     * 查询平台属性
     *
     * @param skuId 商品id
     * @return
     */
    @GetMapping("/api/product/inner/getAttrList/{skuId}")
    public List<BaseAttrInfo> getAttrList(@PathVariable Long skuId);
    
    
    /**
     * 获取商品的分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView 封装了三级分类id及name的对象
     */
    @GetMapping("/api/product/inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable Long category3Id);
    
    /**
     * 查询skuInfo
     *
     * @param skuId 商品id
     * @return SkuInfo
     */
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfoBySkuId(@PathVariable Long skuId);
    
    
    /**
     * 根据商品id查询对应的海报
     *
     * @param spuId 商品id
     * @return spuId符合条件的海报集合
     */
    @GetMapping("/api/product/inner/findSpuPosterBySpuId/{spuId}")
    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId);
    
    
    /**
     * 根据具体商品的id获取最新的售价
     *
     * @param skuId 给定了具体销售属性值的商品id
     * @return BigDecimal类型的售价
     */
    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPriceBySkuId(@PathVariable Long skuId);
}
