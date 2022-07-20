package com.zjgsu.gulimall.coupon.service.impl;

import com.zjgsu.common.to.MemberPrice;
import com.zjgsu.common.to.SkuReductinTo;
import com.zjgsu.gulimall.coupon.entity.MemberPriceEntity;
import com.zjgsu.gulimall.coupon.entity.SkuLadderEntity;
import com.zjgsu.gulimall.coupon.service.MemberPriceService;
import com.zjgsu.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.Query;

import com.zjgsu.gulimall.coupon.dao.SkuFullReductionDao;
import com.zjgsu.gulimall.coupon.entity.SkuFullReductionEntity;
import com.zjgsu.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductinTo skuReductinTo) {
        ////6.4)、sku的优惠、满减信息：gulimall_sms->sms_sku_ladder->sms_sku_full_reduction\sms_member_price
        //1、sms_sku_ladder
//        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
//        BeanUtils.copyProperties(skuReductinTo,skuLadderEntity);
//        skuLadderService.save(skuLadderEntity);
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductinTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductinTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductinTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductinTo.getCountStatus());
        if (skuReductinTo.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }


        //2、sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductinTo,skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.save(skuFullReductionEntity);
        }

        //3、sms_member_price
        List<MemberPrice> memberPrice = skuReductinTo.getMemberPrice();

        List<MemberPriceEntity> memberPriceEntities = memberPrice.stream().map(price -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductinTo.getSkuId());
            memberPriceEntity.setMemberLevelId(price.getId());
            memberPriceEntity.setMemberLevelName(price.getName());
            memberPriceEntity.setMemberPrice(price.getPrice());
            memberPriceEntity.setAddOther(1);

            return memberPriceEntity;
        }).filter(item->{
            return item.getMemberPrice().compareTo(new BigDecimal("0"))==1;
        }).collect(Collectors.toList());

        memberPriceService.saveBatch(memberPriceEntities);
    }

}