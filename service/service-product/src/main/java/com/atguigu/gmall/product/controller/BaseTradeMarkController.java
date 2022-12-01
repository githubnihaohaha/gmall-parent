package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liu-wēi
 * @date: 2022/11/30,16:36
 */
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTradeMarkController {
    
    @Autowired
    private BaseTrademarkService baseTrademarkService;
    
    /**
     * 保存品牌信息
     *
     * @param baseTrademark 品牌实体类
     * @return
     */
    @PostMapping("/save")
    public Result saveTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }
    
    
    /**
     * 分页查询品牌名称
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/{page}/{limit}")
    public Result getBaseTrademarkPageList(@PathVariable Long page,
                                           @PathVariable Long limit) {
        Page<BaseTrademark> iPage = new Page<>(page, limit);
        Page<BaseTrademark> list = baseTrademarkService.selectBaseTrademarkPageList(iPage);
        return Result.ok(list);
    }
    
    /**
     * 获取品牌详情信息
     *
     * @param trademarkId base_trademark 主键
     * @return 主键对应信息
     */
    @GetMapping("/get/{trademarkId}")
    public Result getBaseTradeMarkInfoByTrademarkId(@PathVariable Long trademarkId) {
        BaseTrademark trademark = baseTrademarkService.getById(trademarkId);
        return Result.ok(trademark);
    }
    
    
    /**
     * 修改品牌信息
     *
     * @param baseTrademark 实体类对象
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }
    
    /**
     * 根据id移除品牌信息
     *
     * @param trademarkId base_trademark品牌表主键id
     * @return
     */
    @DeleteMapping("/remove/{trademarkId}")
    public Result removeBaseTrademarkById(@PathVariable Long trademarkId) {
        baseTrademarkService.removeById(trademarkId);
        return Result.ok();
    }
    
}
