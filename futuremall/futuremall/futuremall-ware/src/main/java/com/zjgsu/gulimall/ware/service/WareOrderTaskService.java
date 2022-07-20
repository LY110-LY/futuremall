package com.zjgsu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:46:59
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

