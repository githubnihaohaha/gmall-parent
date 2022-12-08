package com.atguigu.gmall.weball.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.client.ItemFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/5,20:46
 */
@Controller
public class ItemController {
    
    @Autowired
    private ItemFeignClient itemFeignClient;
    
    /**
     * 商品详情页展示
     *
     * @param skuId 商品id
     * @param model 数据共享对象
     * @return 商品详情页
     */
    @RequestMapping("/{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model) {
        
        Result<Map> result = itemFeignClient.getProductInfoBySkuId(skuId);
        model.addAllAttributes(result.getData());
        
        return "item/item";
    }
    
}
