package com.zjgsu.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:38:17
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

