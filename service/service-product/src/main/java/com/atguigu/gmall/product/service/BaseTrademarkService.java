package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: liu-wēi
 * @date: 2022/11/30,16:43
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {
    /**
     * 分页获取品牌信息
     *
     * @param iPage 分页条件
     * @return List<BaseTrademark>
     */
    Page<BaseTrademark> selectBaseTrademarkPageList(Page<BaseTrademark> iPage);
}
