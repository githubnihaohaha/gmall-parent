package com.atguigu.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.cache.GmallCache;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.base.BaseEntity;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuPosterMapper spuPosterMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    
    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;
    
    @Autowired
    private RedissonClient redissonClient;
    
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
        LambdaQueryWrapper<BaseCategory2> wrapper = Wrappers.lambdaQuery(BaseCategory2.class)
                .eq(BaseCategory2::getCategory1Id, category1Id);
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
        LambdaQueryWrapper<BaseCategory3> wrapper = Wrappers.lambdaQuery(BaseCategory3.class)
                .eq(BaseCategory3::getCategory2Id, category2Id);
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateBaseAttrInfo(BaseAttrInfo attrInfo) {
        
        if (attrInfo.getId() == null) {
            // id 为空,添加
            baseAttrInfoMapper.insert(attrInfo);
        } else {
            // id 不为空
            LambdaQueryWrapper<BaseAttrValue> wrapper = Wrappers.lambdaQuery(BaseAttrValue.class)
                    .eq(BaseAttrValue::getAttrId, attrInfo.getId());
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
    
    /**
     * 获取所有销售属性
     *
     * @return
     */
    @Override
    public List<BaseSaleAttr> getSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }
    
    
    /**
     * 保存SPU(商品最小聚合信息)
     *
     * @param spuInfo 包含(销售属性/商品图片/商品海报等)一种商品的信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        
        spuInfoMapper.insert(spuInfo);
        Long spuInfoId = spuInfo.getId();
        
        // 保存商品图片
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList != null) {
            spuImageList.forEach(spuImage -> {
                spuImage.setSpuId(spuInfoId);
                spuImageMapper.insert(spuImage);
            });
        }
        
        // 保存商品海报
        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        if (spuPosterList != null) {
            spuPosterList.forEach(spuPoster -> {
                spuPoster.setSpuId(spuInfoId);
                spuPosterMapper.insert(spuPoster);
            });
        }
        
        // 保存销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList != null) {
            spuSaleAttrList.forEach(spuSaleAttr -> {
                spuSaleAttr.setSpuId(spuInfoId);
                spuSaleAttrMapper.insert(spuSaleAttr);
                
                // 从每个销售属性中获取对应的销售属性值,添加到销售属性值表(spu_sale_attr_value)中
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList != null) {
                    spuSaleAttrValueList.forEach(spuSaleAttrValue -> {
                        spuSaleAttrValue.setSpuId(spuInfoId);
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    });
                }
            });
        }
    }
    
    
    /**
     * 获取商品销售属性值(Spu)
     *
     * @param spuId 商品id
     * @return 某商品对应的多个销售属性及销售属性值
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(Long spuId) {
        
        return spuSaleAttrMapper.selectSpuSaleAttrListBySpuId(spuId);
        
    }
    
    
    /**
     * 分页获取商品库存单元信息
     *
     * @param pageParam 分页条件对象
     * @return IPage 分页结果对象
     */
    @Override
    public IPage<SkuInfo> getSkuPageList(Page<SkuInfo> pageParam) {
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<SkuInfo>().orderByDesc(BaseEntity::getId);
        return skuInfoMapper.selectPage(pageParam, wrapper);
    }
    
    /**
     * 根据商品id查询商品图片
     *
     * @param spuId 商品id
     * @return 商品图片List
     */
    @Override
    public List<SpuImage> getSpuImageListBySpuId(Long spuId) {
        LambdaQueryWrapper<SpuImage> wrapper = new LambdaQueryWrapper<SpuImage>()
                .eq(SpuImage::getSpuId, spuId);
        return spuImageMapper.selectList(wrapper);
    }
    
    
    /**
     * 保存具体商品的信息
     *
     * @param skuInfo 指定了销售属性及销售属性值的具体商品
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        // 存入skuInfo
        skuInfoMapper.insert(skuInfo);
        Long skuInfoId = skuInfo.getId();
        
        // 在集合为null时,输出警告并返回一个初始化的List对象,防止 NPE
        Optional.ofNullable(skuInfo.getSkuImageList())
                .orElseGet(() -> {
                    System.err.println("SkuImageList()为空!!!");
                    return new ArrayList<>();
                })
                .forEach(skuImage -> {
                    skuImage.setSkuId(skuInfoId);
                    skuImageMapper.insert(skuImage);
                });
        
        /*
         * 保存平台属性
         * 判空后进行操作,orElse为防止NPE的兜底集合,如果原集合为null则使用它来.forEach
         *
         * */
        Optional.ofNullable(skuInfo.getSkuAttrValueList()).orElse(new ArrayList<>())
                .forEach(skuAttrValue -> {
                    skuAttrValue.setSkuId(skuInfoId);
                    skuAttrValueMapper.insert(skuAttrValue);
                });
        
        // 保存销售属性值
        Optional.ofNullable(skuInfo.getSkuSaleAttrValueList()).orElse(new ArrayList<>())
                .forEach(skuSaleAttrValue -> {
                    skuSaleAttrValue.setSkuId(skuInfoId);
                    skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                    skuSaleAttrValueMapper.insert(skuSaleAttrValue);
                });
        
        
        // 添加商品成功后,将他的id存入布隆过滤器中,作为今后查询访问时判断是否存在数据的依据
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
        bloomFilter.add(skuInfoId);
    }
    
    
    /**
     * 上架商品
     *
     * @param skuId 商品库存单元id
     * @return
     */
    @Override
    public void onSaleSku(Long skuId) {
        Integer onSaleStatus = 1;
        
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setIsSale(onSaleStatus);
        skuInfo.setId(skuId);
        
        skuInfoMapper.updateById(skuInfo);
        
    }
    
    /**
     * 将商品上架状态值更新为0(下架)
     *
     * @param skuId 库存单元id
     */
    @Override
    public void cancelSaleSku(Long skuId) {
        Integer cancelSaleStatus = 0;
        
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(cancelSaleStatus);
        
        skuInfoMapper.updateById(skuInfo);
    }
    
    
    /**
     * 通过spuId来获取对应的海报信息
     *
     * @param spuId 商品id
     * @return spuId符合条件的海报集合
     */
    @Override
    @GmallCache(prefix = "spuPosterListBySpuId:")
    public List<SpuPoster> getSpuPosterListBySpuId(Long spuId) {
        LambdaQueryWrapper<SpuPoster> wrapper = new LambdaQueryWrapper<SpuPoster>().eq(SpuPoster::getSpuId, spuId);
        return spuPosterMapper.selectList(wrapper);
    }
    
    
    /**
     * 根据具体商品的id获取最新的售价
     *
     * @param skuId 给定了具体销售属性值的商品id
     * @return BigDecimal类型的售价
     */
    @Override
    public BigDecimal getSkuPriceBySkuId(Long skuId) {
        
        RLock lock = redissonClient.getLock(skuId + RedisConst.SKULOCK_SUFFIX);
        lock.lock();
        
        // 加锁访问数据库
        try {
            SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
            if (skuInfo != null) {
                return skuInfo.getPrice();
            }
        } finally {
            lock.unlock();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * 获取商品的分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView 封装了三级分类id及name的对象
     */
    @Override
    @GmallCache(prefix = "categoryView:")
    public BaseCategoryView getCategoryView(Long category3Id) {
        return baseCategoryViewMapper.getCategoryView(category3Id);
    }
    
    /**
     * 查询skuInfo
     *
     * @param skuId 商品id
     * @return SkuInfo
     */
    @Override
    @GmallCache(prefix = "skuInfoBySkuId:")
    public SkuInfo getSkuInfoBySkuId(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo != null) {
            LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<SkuImage>()
                    .eq(SkuImage::getSkuId, skuId);
            
            skuInfo.setSkuImageList(skuImageMapper.selectList(wrapper));
        }
        return skuInfo;
    }
    
    /**
     * 根据商品id查询出对应的平台属性
     *
     * @param skuId 商品id
     * @return 平台属性集合
     */
    @Override
    @GmallCache(prefix = "attrListBySkuId:")
    public List<BaseAttrInfo> getAttrListBySkuId(Long skuId) {
        return baseAttrInfoMapper.getAttrListBySkuId(skuId);
    }
    
    /**
     * 根据spuId查询所有对应的sku关系,以特定的Json格式返回
     *
     * @param spuId
     * @return keyValue格式为 "属性值1|属性值2":"1" 的map
     */
    @Override
    @GmallCache(prefix = "skuValueIdsMap:")
    public Map getSkuValueIdsMap(Long spuId) {
        Map<Object, Object> resultMap = new HashMap<>();
        List<Map> allSkuMapList = skuSaleAttrValueMapper.getSkuValueIdsMap(spuId);
        
        Optional.ofNullable(allSkuMapList).orElse(new ArrayList<>())
                .stream()
                .forEach(map -> resultMap.put(map.get("value_ids"), map.get("sku_id")));
        
        return resultMap;
    }
    
    /**
     * 查询商品的所有销售属性及销售属性值
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @Override
    @GmallCache(prefix = "spuSaleAttrListCheckBySku:")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }
    
    
    /**
     * 获取首页分类数据
     *
     * @return 按不同层级不同类型封装的数据
     */
    @Override
    public List<JSONObject> getCategoryList() {
        
        final String categoryId = "categoryId";
        final String categoryName = "categoryName";
        final String categoryChild = "categoryChild";
        
        // 从视图对象中查询出所有级别的数据
        List<BaseCategoryView> categoryViews = baseCategoryViewMapper.selectList(Wrappers.emptyWrapper());
        
        // 存放结果的集合
        ArrayList<JSONObject> objectArrayList = new ArrayList<>();
        
        // 初始化index
        AtomicInteger index = new AtomicInteger(0);
        
        // 将数据使用一级id进行分组
        Map<Long, List<BaseCategoryView>> groupBy1IdViewMap = categoryViews.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        
        // 封装数据(每个一级分类下有多个二级分类,二级分类下有多个三级分类,将这些子分类保存至Child中)
        groupBy1IdViewMap.forEach((key, categoryViewsList1) -> {
            // 一级分类
            JSONObject category1JsonObj = new JSONObject();
            category1JsonObj.put(categoryId, key);
            category1JsonObj.put(categoryName, categoryViewsList1.get(0).getCategory1Name());
            category1JsonObj.put("index", index);
            
            index.incrementAndGet(); // index +1
            
            // 封装每个一级分类下的所有二级分类
            Map<Long, List<BaseCategoryView>> groupBy2IdViewMap = categoryViewsList1.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            ArrayList<JSONObject> category2List = new ArrayList<>();
            groupBy2IdViewMap.forEach((key1, categoryViewsList2) -> {
                JSONObject category2JsonObj = new JSONObject();
                category2JsonObj.put(categoryId, key);
                category2JsonObj.put(categoryName, categoryViewsList2.get(0).getCategory2Name());
                
                // 封装每个二级分类下的三级分类
                ArrayList<JSONObject> category3List = new ArrayList<>();
                categoryViewsList2.forEach(category3 -> {
                    JSONObject category3JsonObj = new JSONObject();
                    category3JsonObj.put(categoryId, category3.getCategory3Id());
                    category3JsonObj.put(categoryName, category3.getCategory3Name());
                    category3List.add(category3JsonObj);
                });
                category2JsonObj.put(categoryChild, category3List);
                
                
                category2List.add(category2JsonObj);
            });
            
            category1JsonObj.put(categoryChild, category2List);
            
            objectArrayList.add(category1JsonObj);
        });
        return objectArrayList;
    }
}
