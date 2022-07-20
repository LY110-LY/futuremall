package com.zjgsu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.product.entity.AttrGroupEntity;
import com.zjgsu.gulimall.product.vo.AttrGroupRelationVo;
import com.zjgsu.gulimall.product.vo.AttrGroupWithArrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 10:35:53
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    void deleteRelation(AttrGroupRelationVo[] vos);


    List<AttrGroupWithArrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

