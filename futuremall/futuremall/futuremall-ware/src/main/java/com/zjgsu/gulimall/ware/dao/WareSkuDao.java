package com.zjgsu.gulimall.ware.dao;

import com.zjgsu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:46:59
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addTock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkuStock(@Param("skuId") Long skuId);
}
