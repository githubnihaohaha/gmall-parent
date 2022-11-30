package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/30,21:08
 */
@RestController
@RequestMapping("/admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {
    
    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;
    
    /**
     * 根据三级id查询还未被关联的品牌
     *
     * @param category3Id 三级id
     * @return
     */
    @GetMapping("/findCurrentTrademarkList/{category3Id}")
    public Result findCurrentTrademarkList(@PathVariable Long category3Id) {
        List<BaseTrademark> trademarkList = baseCategoryTrademarkService.findCurrentTrademarkList(category3Id);
        return Result.ok(trademarkList);
    }
    
    
    /**
     * 添加分类品牌关联
     *
     * @param categoryTrademarkVo 实体类
     * @return
     */
    @PostMapping("/save")
    public Result saveBaseCategoryTrademark(@RequestBody CategoryTrademarkVo categoryTrademarkVo) {
        if (categoryTrademarkVo != null) {
            baseCategoryTrademarkService.saveBaseCategoryTrademark(categoryTrademarkVo);
        }
        return Result.ok();
    }
    
    
    /**
     * 移除分类品牌关联
     *
     * @param category3Id 类别id
     * @param trademarkId 品牌id
     * @return
     */
    @DeleteMapping("/remove/{category3Id}/{trademarkId}")
    public Result removeCategoryTrademark(@PathVariable Long category3Id,
                                          @PathVariable Long trademarkId) {
        baseCategoryTrademarkService.removeCategoryTrademark(category3Id, trademarkId);
        return Result.ok();
    }
    
    
    /**
     * 根据第三级id查询出对应的品牌
     *
     * @param category3Id 三级分类id
     * @return 品牌List集合
     */
    @GetMapping("/findTrademarkList/{category3Id}")
    public Result selectTrademarkListByCategory3Id(@PathVariable Long category3Id) {
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.getTrademarkListByCategory3Id(category3Id);
        return Result.ok(baseTrademarkList);
    }
}
