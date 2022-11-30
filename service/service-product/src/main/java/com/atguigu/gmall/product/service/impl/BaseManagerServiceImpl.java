package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/28,16:37
 */
@Service
public class BaseManagerServiceImpl implements BaseManagerService {
    
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;
    
    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;
    
    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;
    
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    
    /**
     * 获取一级类别数据
     *
     * @return 一级类别数据
     */
    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategory1Mapper.selectList(null);
    }
    
    
    /**
     * 通过一级类别的id查询二级类别数据
     *
     * @param category1Id 一级类别id
     * @return 二级类别List
     */
    @Override
    public List<BaseCategory2> getCategory2ByCategory1Id(Long category1Id) {
        LambdaQueryWrapper<BaseCategory2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategory2::getCategory1Id, category1Id);
        return baseCategory2Mapper.selectList(wrapper);
    }
    
    
    /**
     * 通过二级类别的id查询三级类别数据
     *
     * @param category2Id 二级类别id
     * @return 三级类别List
     */
    @Override
    public List<BaseCategory3> getCategory3ByCategory2Id(Long category2Id) {
        LambdaQueryWrapper<BaseCategory3> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategory3::getCategory2Id, category2Id);
        return baseCategory3Mapper.selectList(wrapper);
    }
    
    /**
     * 根据传入的条件查询平台属性的集合
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return 封装不同平台属性的list集合
     */
    @Override
    public List<BaseAttrInfo> getAttrInfoListByCritria(Long category1Id, Long category2Id, Long category3Id) {
        
        return baseAttrInfoMapper.selectAttrInfoList(category1Id, category2Id, category3Id);
        
        
    }
    
    
    /**
     * 添加或修改平台属性信息
     *
     * @param attrInfo BaseAttrInfo实体类
     */
    @Transactional(rollbackFor = Exception.class) // 开启事务,所有异常都回滚
    @Override
    public void saveOrUpdateBaseAttrInfo(BaseAttrInfo attrInfo) {
        
        if (attrInfo.getId() == null) {
            // id 为空,添加
            baseAttrInfoMapper.insert(attrInfo);
        } else {
            // id 不为空
            LambdaQueryWrapper<BaseAttrValue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BaseAttrValue::getAttrId, attrInfo.getId());
            baseAttrValueMapper.delete(wrapper);
            baseAttrInfoMapper.updateById(attrInfo);
        }
        
        attrInfo.getAttrValueList().forEach(item -> {
            item.setAttrId(attrInfo.getId());
            baseAttrValueMapper.insert(item);
        });
    }
    
    /**
     * 根据平台属性id查询平台属性值信息
     *
     * @param attrInfoId 平台属性id
     * @return List<BaseAttrValue>
     */
    @Override
    public List<BaseAttrValue> getAttrValueByAttrInfoId(Long attrInfoId) {
        
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrInfoId);
        
        // 如果平台属性不存在,平台属性对于的值也应该为空
        if (baseAttrInfo == null) {
            return Collections.emptyList();
        }
        
        LambdaQueryWrapper<BaseAttrValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseAttrValue::getAttrId, attrInfoId);
        return baseAttrValueMapper.selectList(wrapper);
    }
    
    /**
     * 查询商品属性信息 SPU
     *
     * @param page    分页条件
     * @param spuInfo 封装条件的实体类
     * @return IPage<SpuInfo>
     */
    @Override
    public IPage<SpuInfo> getSpuInfoPageList(Page<SpuInfo> page, SpuInfo spuInfo) {
        LambdaQueryWrapper<SpuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpuInfo::getCategory3Id, spuInfo.getCategory3Id());
        
        return spuInfoMapper.selectPage(page, wrapper);
    }
}
