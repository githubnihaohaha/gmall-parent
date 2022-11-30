package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        IPage<SpuInfo> infoIPageList = baseManagerService.getSpuInfoPageList(page,spuInfo);
        return Result.ok(infoIPageList);
    }
}
