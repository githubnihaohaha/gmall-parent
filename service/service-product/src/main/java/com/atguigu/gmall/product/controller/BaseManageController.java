package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.BaseManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/28,16:24
 */
@RestController
@RequestMapping("/admin/product")
public class BaseManageController {
    
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
    public Result getCategory3(@PathVariable Long category2Id) {
        List<BaseCategory3> category3List = baseManagerService.getCategory3ByCategory2Id(category2Id);
        return Result.ok(category3List);
    }
    
    
    /**
     * 根据传入的条件查询平台属性的集合
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return 封装不同平台属性的list集合
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getBaseAttrInfoList(@PathVariable Long category1Id,
                                      @PathVariable Long category2Id,
                                      @PathVariable Long category3Id) {
        
        List<BaseAttrInfo> attrInfoList
                = baseManagerService.getAttrInfoListByCritria(category1Id, category2Id, category3Id);
        return Result.ok(attrInfoList);
        
    }
    
    
    /**
     * 添加或修改平台属性信息
     *
     * @param attrInfo BaseAttrInfo实体类
     * @return
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo attrInfo) {
        if (attrInfo == null) {
            throw new GmallException("参数为空!", 20001);
        }
        baseManagerService.saveOrUpdateBaseAttrInfo(attrInfo);
        return Result.ok();
    }
    
    
    /**
     * 根据平台属性id查询平台属性值信息
     *
     * @param attrInfoId 平台属性id
     * @return List<BaseAttrValue>
     */
    @GetMapping("/getAttrValueList/{attrInfoId}")
    public Result getAttrValueList(@PathVariable Long attrInfoId) {
        List<BaseAttrValue> attrValueList = baseManagerService.getAttrValueByAttrInfoId(attrInfoId);
        return Result.ok(attrValueList);
    }
    
}
