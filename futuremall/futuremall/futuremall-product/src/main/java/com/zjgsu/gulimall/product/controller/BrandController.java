package com.zjgsu.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zjgsu.common.valid.AddGroup;
import com.zjgsu.common.valid.UpdateGroup;
import com.zjgsu.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.zjgsu.gulimall.product.entity.BrandEntity;
import com.zjgsu.gulimall.product.service.BrandService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 11:23:40
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    @GetMapping("/info")
    public R info(@RequestParam("brandIds") List<Long> brandIds){
        List<BrandEntity> brand = brandService.getBrandByIds(brandIds);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand/*, BindingResult result*/){
//        if (result.hasErrors()){
//            Map<String, String> map = new HashMap<>();
//            //1、获取校验的错误结果
//            result.getFieldErrors().forEach((item)->{
//                //FieldError 获取到错误提示
//                String message = item.getDefaultMessage();
//                //获取错误的属性的名字
//                String field = item.getField();
//                map.put(field,message);
//            });
//            return R.error(400,"提交的数据不合法").put("data",map);
//        }else {
//            brandService.save(brand);
//        }
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated({UpdateStatusGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
