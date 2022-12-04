package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/12/1,16:27
 */
@Repository
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {
    /**
     * 获取商品销售属性值(Spu)
     *
     * @param spuId 商品id
     * @return 某商品对应的多个销售属性及销售属性值
     */
    List<SpuSaleAttr> selectSpuSaleAttrListBySpuId(Long spuId);
    
    /**
     * 查询商品的所有销售属性及销售属性值
     *
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@Param("skuId") Long skuId,
                                                   @Param("spuId") Long spuId);
}
