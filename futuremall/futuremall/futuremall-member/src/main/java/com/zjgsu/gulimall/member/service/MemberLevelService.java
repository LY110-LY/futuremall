package com.zjgsu.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.gulimall.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:25:49
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

