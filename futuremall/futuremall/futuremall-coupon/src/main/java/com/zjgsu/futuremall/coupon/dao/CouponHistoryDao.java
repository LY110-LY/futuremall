package com.zjgsu.gulimall.coupon.dao;

import com.zjgsu.gulimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:12:53
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
