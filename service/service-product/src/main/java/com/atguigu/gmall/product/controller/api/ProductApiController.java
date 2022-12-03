package com.atguigu.gmall.product.controller.api;

import com.atguigu.gmall.model.product.SpuPoster;
import com.atguigu.gmall.product.service.BaseManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/12/3,17:30
 */
@RestController
@RequestMapping("/api/product/inner")
public class ProductApiController {
    
    @Autowired
    private BaseManagerService baseManagerService;
    
    
    /**
     * 根据商品id查询对应的海报
     *
     * @param spuId 商品id
     * @return spuId符合条件的海报集合
     */
    @GetMapping("/findSpuPosterBySpuId/{spuId}")
    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId) {
        return baseManagerService.getSpuPosterListBySpuId(spuId);
    }
    
    
    /**
     * 根据具体商品的id获取最新的售价
     *
     * @param skuId 给定了具体销售属性值的商品id
     * @return BigDecimal类型的售价
     */
    @GetMapping("/getSkuPrice/{skuId}")
    public BigDecimal getSkuPriceBySkuId(@PathVariable Long skuId) {
        return baseManagerService.getSkuPriceBySkuId(skuId);
    }
}
