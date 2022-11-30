package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/28,16:37
 */
public interface BaseManagerService {
    /**
     * 获取一级类别数据
     *
     * @return 一级类别数据
     */
    List<BaseCategory1> getCategory1();
    
    
    /**
     * 通过一级类别的id查询二级类别数据
     *
     * @param category1Id 一级类别id
     * @return 二级类别List
     */
    List<BaseCategory2> getCategory2ByCategory1Id(Long category1Id);
    
    
    /**
     * 通过二级类别的id查询三级类别数据
     *
     * @param category2Id 二级类别id
     * @return 三级类别List
     */
    List<BaseCategory3> getCategory3ByCategory2Id(Long category2Id);
    
    
    /**
     * 根据传入的条件查询平台属性的集合
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return 封装不同平台属性的list集合
     */
    List<BaseAttrInfo> getAttrInfoListByCritria(Long category1Id, Long category2Id, Long category3Id);
    
    /**
     * 添加或修改平台属性信息
     *
     * @param attrInfo BaseAttrInfo实体类
     * @return
     */
    void saveOrUpdateBaseAttrInfo(BaseAttrInfo attrInfo);
    
    /**
     * 根据平台属性id查询平台属性值信息
     *
     * @param attrInfoId 平台属性id
     * @return List<BaseAttrValue>
     */
    List<BaseAttrValue> getAttrValueByAttrInfoId(Long attrInfoId);
    
    
    /**
     * 查询商品属性信息 SPU
     *
     * @param page    分页条件
     * @param spuInfo 封装条件的实体类
     * @return IPage<SpuInfo>
     */
    IPage<SpuInfo> getSpuInfoPageList(Page<SpuInfo> page, SpuInfo spuInfo);
}
