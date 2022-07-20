package com.zjgsu.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.to.SkuReductinTo;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:12:53
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductinTo skuReductinTo);
}

