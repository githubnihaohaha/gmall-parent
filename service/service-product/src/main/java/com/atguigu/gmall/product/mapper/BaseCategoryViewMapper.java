package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategoryView;
import org.springframework.stereotype.Repository;

/**
 * @author: liu-wēi
 * @date: 2022/12/3,20:53
 */
@Repository
public interface BaseCategoryViewMapper {
    /**
     * 获取商品分类信息
     *
     * @param category3Id 根据三级id
     * @return SQL视图对象, 封装了三级基本信息
     */
    BaseCategoryView getCategoryView(Long category3Id);
}
