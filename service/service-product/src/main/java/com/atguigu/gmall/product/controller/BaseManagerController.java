package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/28,16:24
 */
@RestController
@RequestMapping("/admin/product")
public class BaseManagerController {
    
    @Autowired
    private BaseManagerService baseManagerService;
    
    /**
     * 获取一级类别数据
     *
     * @return 一级类别数据
     */
    @GetMapping("/getCategory1")
    public Result getCategory1() {
        List<BaseCategory1> category1List = baseManagerService.getCategory1();
        return Result.ok(category1List);
    }
    
    
    /**
     * 通过一级类别的id查询二级类别数据
     *
     * @param category1Id 一级类别id
     * @return 二级类别List
     */
    @GetMapping("/getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable Long category1Id) {
        List<BaseCategory2> category2List = baseManagerService.getCategory2ByCategory1Id(category1Id);
        return Result.ok(category2List);
    }
    
    
    /**
     * 通过二级类别的id查询三级类别数据
     *
     * @param category2Id 二级类别id
     * @return 三级类别List
     */
    @GetMapping("/getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable Long category2Id){
        List<BaseCategory3> category3List = baseManagerService.getCategory3ByCategory2Id(category2Id);
        return Result.ok(category3List);
    }
    
}
