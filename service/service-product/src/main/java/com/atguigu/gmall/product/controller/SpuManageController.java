package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/29,20:53
 */
@RestController
@RequestMapping("/admin/product")
public class SpuManageController {
    
    @Autowired
    private BaseManagerService baseManagerService;
    
    
    /**
     * 保存 SpuInfo
     *
     * @param spuInfo 实体类
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        if (spuInfo != null) {
            baseManagerService.saveSpuInfo(spuInfo);
        }
        return Result.ok();
    }
    
    
    /**
     * 获取所有销售属性
     *
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result getSaleAttrList() {
        List<BaseSaleAttr> saleAttrList = baseManagerService.getSaleAttrList();
        return Result.ok(saleAttrList);
    }
    
    
    /**
     * 查询 SPU 商品属性信息
     *
     * @param current 当前页
     * @param size    每页总条数
     * @param spuInfo 封装条件的实体类
     * @return
     */
    @GetMapping("/{current}/{size}")
    public Result getSpuInfoPageList(@PathVariable Long current,
                                     @PathVariable Long size,
                                     SpuInfo spuInfo) {
        Page<SpuInfo> page = new Page<>(current, size);
        IPage<SpuInfo> infoIPageList = baseManagerService.getSpuInfoPageList(page, spuInfo);
        return Result.ok(infoIPageList);
    }
}
