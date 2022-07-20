package com.zjgsu.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.coupon.entity.CouponSpuRelationEntity;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:12:53
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

