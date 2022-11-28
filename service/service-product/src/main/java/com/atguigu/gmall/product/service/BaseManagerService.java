package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;

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
}
