package com.zjgsu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.product.entity.ProductAttrValueEntity;
import com.zjgsu.gulimall.product.vo.BaseAttrs;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 10:35:53
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveProdutAttr(List<BaseAttrs> baseAttrs);

    List<ProductAttrValueEntity> baseAttrListforspu(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}

