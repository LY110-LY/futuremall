package com.zjgsu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zjgsu.gulimall.product.entity.ProductAttrValueEntity;
import com.zjgsu.gulimall.product.service.ProductAttrValueService;
import com.zjgsu.gulimall.product.vo.AttrRespVo;
import com.zjgsu.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zjgsu.gulimall.product.entity.AttrEntity;
import com.zjgsu.gulimall.product.service.AttrService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.R;



/**
 * 商品属性
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 11:23:40
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;


    ///product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrListforspu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrListforspu(spuId);
        return R.ok().put("data",entities);
    }


    //product/attr/sale/list/{catelogId}
    //product/attr/base/list/{catelogId}
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String type){
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, type);
        return R.ok().put("page",page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);

        AttrRespVo respVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
//		attrService.updateById(attr);

        attrService.updateAttr(attr);
        return R.ok();
    }

    ///product/attr/update/{spuId}
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId, entities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
