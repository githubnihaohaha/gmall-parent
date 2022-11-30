package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/30,21:12
 */
public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {
    /**
     * 根据第三级id查询出对应的品牌
     *
     * @param category3Id 三级分类id
     * @return 品牌List集合
     */
    List<BaseTrademark> getTrademarkListByCategory3Id(Long category3Id);
    
    /**
     * 移除分类品牌关联
     *
     * @param category3Id 类别id
     * @param trademarkId 品牌id
     * @return
     */
    void removeCategoryTrademark(Long category3Id, Long trademarkId);
    
    /**
     * 根据三级id查询还未被关联的品牌
     *
     * @param category3Id 三级id
     * @return
     */
    List<BaseTrademark> findCurrentTrademarkList(Long category3Id);
    
    /**
     * 添加分类品牌关联
     *
     * @param categoryTrademarkVo 实体类
     * @return
     */
    void saveBaseCategoryTrademark(CategoryTrademarkVo categoryTrademarkVo);
}
