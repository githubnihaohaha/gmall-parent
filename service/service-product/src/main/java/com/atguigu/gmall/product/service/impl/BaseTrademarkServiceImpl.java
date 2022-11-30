package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTradeMarkMapper;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: liu-wēi
 * @date: 2022/11/30,16:44
 */
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTradeMarkMapper, BaseTrademark> implements BaseTrademarkService {
    
    /**
     * 分页获取品牌信息
     *
     * @param iPage 分页条件
     * @return List<BaseTrademark>
     */
    @Override
    public Page<BaseTrademark> selectBaseTrademarkPageList(Page<BaseTrademark> iPage) {
        return baseMapper.selectPage(iPage,null);
    }
}
