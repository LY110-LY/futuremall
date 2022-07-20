package com.zjgsu.gulimall.member.dao;

import com.zjgsu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author liangyi
 * @email ly120614ly@gmail.com
 * @date 2022-05-09 14:25:49
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
