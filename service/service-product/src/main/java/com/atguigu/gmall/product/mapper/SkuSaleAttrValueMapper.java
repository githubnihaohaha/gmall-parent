package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: liu-wēi
 * @date: 2022/12/2,19:58
 */
@Repository
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {
    /**
     * 返回这个商品集的所有具体商品
     *
     * @param spuId
     * @return 多个map集合
     */
    List<Map> getSkuValueIdsMap(Long spuId);
}
