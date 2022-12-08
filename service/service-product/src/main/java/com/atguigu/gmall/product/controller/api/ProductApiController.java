package com.atguigu.gmall.product.controller.api;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.BaseManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/3,17:30
 */
@RestController
@RequestMapping("/api/product")
public class ProductApiController {
    
    @Autowired
    private BaseManagerService baseManagerService;
    
    /**
     * 查询商品的所有销售属性及属性值
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @GetMapping("/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId,
                                                          @PathVariable Long spuId) {
        return baseManagerService.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }
    
    /**
     * 根据spuId查询所有对应的sku关系,以特定的Json格式返回
     *
     * @param spuId
     * @return keyValue格式为 "属性值1|属性值2":"1" 的map
     */
    @GetMapping("/inner/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable Long spuId) {
        return baseManagerService.getSkuValueIdsMap(spuId);
    }
    
    /**
     * 查询平台属性
     *
     * @param skuId 商品id
     * @return
     */
    @GetMapping("/inner/getAttrList/{skuId}")
    public List<BaseAttrInfo> getAttrList(@PathVariable Long skuId) {
        return baseManagerService.getAttrListBySkuId(skuId);
    }
    
    
    /**
     * 获取商品的分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView 封装了三级分类id及name的对象
     */
    @GetMapping("/inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable Long category3Id) {
        return baseManagerService.getCategoryView(category3Id);
    }
    
    /**
     * 查询skuInfo
     *
     * @param skuId 商品id
     * @return SkuInfo
     */
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfoBySkuId(@PathVariable Long skuId) {
        return baseManagerService.getSkuInfoBySkuId(skuId);
    }
    
    
    /**
     * 根据商品id查询对应的海报
     *
     * @param spuId 商品id
     * @return spuId符合条件的海报集合
     */
    @GetMapping("/inner/findSpuPosterBySpuId/{spuId}")
    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId) {
        return baseManagerService.getSpuPosterListBySpuId(spuId);
    }
    
    
    /**
     * 根据具体商品的id获取最新的售价
     *
     * @param skuId 给定了具体销售属性值的商品id
     * @return BigDecimal类型的售价
     */
    @GetMapping("/inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPriceBySkuId(@PathVariable Long skuId) {
        return baseManagerService.getSkuPriceBySkuId(skuId);
    }
}
