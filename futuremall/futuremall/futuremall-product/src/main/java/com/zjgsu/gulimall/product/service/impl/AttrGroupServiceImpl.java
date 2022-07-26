package com.zjgsu.gulimall.product.service.impl;

import com.zjgsu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zjgsu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zjgsu.gulimall.product.entity.AttrEntity;
import com.zjgsu.gulimall.product.service.AttrService;
import com.zjgsu.gulimall.product.vo.AttrGroupRelationVo;
import com.zjgsu.gulimall.product.vo.AttrGroupWithArrsVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.Query;

import com.zjgsu.gulimall.product.dao.AttrGroupDao;
import com.zjgsu.gulimall.product.entity.AttrGroupEntity;
import com.zjgsu.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (StringUtils.hasLength(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_Id",catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }

    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        //单一删除要不停调用，实现批量删除
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entities);
    }

    /**
     * 根据分类id查出所有的分组以及这些组里面的属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithArrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        //1、查询分组信息
        List<AttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //2、查询所有属性
        List<AttrGroupWithArrsVo> collect = groupEntities.stream().map((group) -> {
            AttrGroupWithArrsVo attrGroupWithArrsVo = new AttrGroupWithArrsVo();
            BeanUtils.copyProperties(group,attrGroupWithArrsVo);

            List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithArrsVo.getAttrGroupId());
            attrGroupWithArrsVo.setAttrs(attrs);
            return attrGroupWithArrsVo;
        }).collect(Collectors.toList());

        return collect;
    }


}