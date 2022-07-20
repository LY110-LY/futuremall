package com.zjgsu.gulimall.order.dao;

import com.zjgsu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:38:17
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
