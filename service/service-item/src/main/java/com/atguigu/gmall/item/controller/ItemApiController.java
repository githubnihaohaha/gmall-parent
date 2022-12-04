package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/4,15:54
 */
@RestController
@RequestMapping("/api/item")
public class ItemApiController {
    
    @Autowired
    private ItemService itemService;
    
    /**
     * 查询商品所有信息
     *
     * @param skuId 库存单元id
     * @return
     */
    @GetMapping("/{skuId}")
    public Map<String,Object> getProductInfoBySkuId(@PathVariable Long skuId) {
        return itemService.getProductInfoBySkuId(skuId);
    }
    
}
