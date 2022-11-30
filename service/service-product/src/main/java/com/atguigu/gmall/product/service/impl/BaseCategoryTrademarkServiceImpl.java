package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.atguigu.gmall.product.mapper.BaseTradeMarkMapper;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: liu-wēi
 * @date: 2022/11/30,21:14
 */
@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark>
        implements BaseCategoryTrademarkService {
    
    @Autowired
    private BaseCategoryTrademarkMapper baseCategoryTrademarkMapper;
    
    @Autowired
    private BaseTradeMarkMapper baseTradeMarkMapper;
    
    /**
     * 根据第三级id查询出对应的品牌
     *
     * @param category3Id 三级分类id
     * @return 品牌List集合
     */
    @Override
    public List<BaseTrademark> getTrademarkListByCategory3Id(Long category3Id) {
        
        LambdaQueryWrapper<BaseCategoryTrademark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        
        List<BaseCategoryTrademark> baseCategoryTrademarks = baseCategoryTrademarkMapper.selectList(wrapper);
        
        if (baseCategoryTrademarks == null) {
            return Collections.emptyList();
        }
        
        // 在中间表中通过 category3Id 查询出对应的多个对象,获取他们的 trademarkId
        List<Long> trademarkIds = baseCategoryTrademarks
                .stream()
                .map(BaseCategoryTrademark::getTrademarkId)
                .collect(Collectors.toList());
        
        // 如果没有对应的id,直接返回空集合
        if (CollectionUtils.isEmpty(trademarkIds)) {
            return Collections.emptyList();
        }
        
        // 使用 tradeId 查询出对应的品牌名称并返回
        return baseTradeMarkMapper.selectBatchIds(trademarkIds);
    }
    
    
    /**
     * 移除分类品牌关联
     *
     * @param category3Id 类别id
     * @param trademarkId 品牌id
     * @return
     */
    @Override
    public void removeCategoryTrademark(Long category3Id, Long trademarkId) {
        LambdaQueryWrapper<BaseCategoryTrademark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id)
                .eq(BaseCategoryTrademark::getTrademarkId, trademarkId);
        baseCategoryTrademarkMapper.delete(wrapper);
    }
    
    /**
     * 根据三级id查询还未被关联的品牌
     *
     * @param category3Id 三级id
     * @return
     */
    @Override
    public List<BaseTrademark> findCurrentTrademarkList(Long category3Id) {
        LambdaQueryWrapper<BaseCategoryTrademark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        
        // 根据三级id查询出目前已经关联的品牌
        List<BaseCategoryTrademark> baseCategoryTrademarks = baseCategoryTrademarkMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(baseCategoryTrademarks)) {
            List<Long> trademarkIds = baseCategoryTrademarks.stream()
                    .map(BaseCategoryTrademark::getTrademarkId)
                    .collect(Collectors.toList());
            
            // 查询所有品牌,将当前没有关联的品牌查询筛选出来并返回
            return baseTradeMarkMapper.selectList(null)
                    .stream()
                    .filter(baseTrademark -> !trademarkIds.contains(baseTrademark.getId()))
                    .collect(Collectors.toList());
        }
        // 如果当前的三级id没有关联任何的品牌,给他返回所有的品牌
        return baseTradeMarkMapper.selectList(null);
    }
    
    /**
     * 添加分类品牌关联
     *
     * @param categoryTrademarkVo 实体类
     * @return
     */
    @Override
    public void saveBaseCategoryTrademark(CategoryTrademarkVo categoryTrademarkVo) {
        // 获取要关联的品牌id
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
        
        // 每个品牌id创建一个对象,最后批量存入即可
        if (!CollectionUtils.isEmpty(trademarkIdList)) {
            List<BaseCategoryTrademark> baseCategoryTrademarkList = trademarkIdList
                    .stream()
                    .map(trademarkId -> {
                        BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
                        baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
                        baseCategoryTrademark.setTrademarkId(trademarkId);
                        return baseCategoryTrademark;
                    }).collect(Collectors.toList());
            
            this.saveBatch(baseCategoryTrademarkList);
        }
    }
}
