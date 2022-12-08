package com.atguigu.gmall.weball.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: liu-wēi
 * @date: 8 Dec 2022,13:38
 */
@Controller
public class IndexController {
    
    @Resource
    private ProductFeignClient productFeignClient;
    
    /**
     * 首页三级分类展示
     *
     * @param model 数据传输对象
     * @return 跳转至首页
     */
    @GetMapping({"/", "/index.html"})
    public String toIndex(Model model) {
        Result<List<JSONObject>> result = productFeignClient.getCategoryList();
        model.addAttribute("list", result.getData());
        return "index/index";
    }
    
}
