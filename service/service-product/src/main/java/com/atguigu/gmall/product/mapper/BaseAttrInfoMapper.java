package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/11/29,16:46
 */
@Repository
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
    /**
     * 根据传入的条件进行查询,可以为空
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return List<BaseAttrInfo>
     */
    List<BaseAttrInfo> selectAttrInfoList(
            @Param("category1Id") Long category1Id,
            @Param("category2Id") Long category2Id,
            @Param("category3Id") Long category3Id);
}
