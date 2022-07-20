package com.zjgsu.gulimall.product.dao;

import com.zjgsu.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 10:35:53
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
