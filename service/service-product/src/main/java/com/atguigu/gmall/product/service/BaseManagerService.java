package com.atguigu.gmall.product.service;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    
    /**
     * 获取所有销售属性
     *
     * @return
     */
    List<BaseSaleAttr> getSaleAttrList();
    
    /**
     * 保存SPU(商品最小聚合信息)
     *
     * @param spuInfo 包含(销售属性/商品图片/商品海报等)一种商品的信息
     */
    void saveSpuInfo(SpuInfo spuInfo);
    
    /**
     * 获取商品销售属性值(Spu)
     *
     * @param spuId 商品id
     * @return 某商品对应的多个销售属性及销售属性值
     */
    List<SpuSaleAttr> getSpuSaleAttrBySpuId(Long spuId);
    
    /**
     * 分页获取商品库存单元信息
     *
     * @param pageParam 分页条件对象
     * @return IPage 分页结果对象
     */
    IPage<SkuInfo> getSkuPageList(Page<SkuInfo> pageParam);
    
    /**
     * 根据商品id查询商品图片
     *
     * @param spuId 商品id
     * @return 商品图片List
     */
    List<SpuImage> getSpuImageListBySpuId(Long spuId);
    
    /**
     * 保存具体商品的信息
     *
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);
    
    /**
     * 将商品上架状态值更新为1(上架)
     *
     * @param skuId 商品库存单元id
     */
    void onSaleSku(Long skuId);
    
    /**
     * 将商品上架状态值更新为0(下架)
     *
     * @param skuId 库存单元id
     */
    void cancelSaleSku(Long skuId);
    
    
    /**
     * 通过spuId来获取对应的海报信息
     *
     * @param spuId 商品id
     * @return spuId符合条件的海报集合
     */
    List<SpuPoster> getSpuPosterListBySpuId(Long spuId);
    
    /**
     * 根据具体商品的id获取最新的售价
     *
     * @param skuId 给定了具体销售属性值的商品id
     * @return BigDecimal类型的售价
     */
    BigDecimal getSkuPriceBySkuId(Long skuId);
    
    /**
     * 获取商品的分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView 封装了三级分类id及name的对象
     */
    BaseCategoryView getCategoryView(Long category3Id);
    
    /**
     * 查询skuInfo
     *
     * @param skuId 商品id
     * @return SkuInfo
     */
    SkuInfo getSkuInfoBySkuId(Long skuId);
    
    /**
     * 根据商品id查询出对应的平台属性
     *
     * @param skuId 商品id
     * @return 平台属性集合
     */
    List<BaseAttrInfo> getAttrListBySkuId(Long skuId);
    
    /**
     * 根据spuId查询所有对应的sku关系,以特定的Json格式返回
     *
     * @param spuId
     * @return keyValue格式为 "属性值1|属性值2":"1" 的map
     */
    Map getSkuValueIdsMap(Long spuId);
    
    /**
     * 查询商品的所有销售属性及销售属性值
     *
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);
    
    
    /**
     * 获取首页分类数据
     *
     * @return 按不同层级不同类型封装的数据
     */
    List<JSONObject> getCategoryList();
}
