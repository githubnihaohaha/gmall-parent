package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liu-wēi
 * @date: 2022/12/2,16:16
 */
@RestController
@RequestMapping("/admin/product")
public class SkuManageController {
    
    @Autowired
    private BaseManagerService baseManagerService;
    
    /**
     * 下架商品
     *
     * @param skuId 商品库存单元id
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId) {
        baseManagerService.cancelSaleSku(skuId);
        return Result.ok();
    }
    
    
    /**
     * 上架商品
     *
     * @param skuId 商品库存单元id
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSaleSku(@PathVariable Long skuId) {
        baseManagerService.onSaleSku(skuId);
        return Result.ok();
    }
    
    
    /**
     * 保存库存单元信息
     *
     * @param skuInfo 库存单元实体类对象
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        baseManagerService.saveSkuInfo(skuInfo);
        return Result.ok();
    }
    
    
    /**
     * 根据商品id查询商品图片
     *
     * @param spuId 商品id
     * @return 商品图片List
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable Long spuId) {
        List<SpuImage> imageList = baseManagerService.getSpuImageListBySpuId(spuId);
        return Result.ok(imageList);
    }
    
    
    /**
     * 获取商品销售属性值(Spu)
     *
     * @param spuId 商品id
     * @return 某商品对应的多个销售属性及销售属性值
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrBySpuId(@PathVariable Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = baseManagerService.getSpuSaleAttrBySpuId(spuId);
        return Result.ok(spuSaleAttrList);
    }
    
    
    /**
     * 分页获取商品库存单元信息
     *
     * @param page  当前索引页
     * @param limit 每页显示条数
     * @return 分页对象
     */
    @GetMapping("/list/{page}/{limit}")
    public Result getSkuPageList(@PathVariable Long page,
                                 @PathVariable Long limit) {
        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> skuInfoIPage = baseManagerService.getSkuPageList(pageParam);
        return Result.ok(skuInfoIPage);
    }
}
