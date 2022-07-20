package com.zjgsu.gulimall.ware.service.impl;

import com.zjgsu.common.constant.WareConstant;
import com.zjgsu.gulimall.ware.entity.PurchaseDetailEntity;
import com.zjgsu.gulimall.ware.service.PurchaseDetailService;
import com.zjgsu.gulimall.ware.service.WareSkuService;
import com.zjgsu.gulimall.ware.vo.MergeVo;
import com.zjgsu.gulimall.ware.vo.PurchaseDoneVo;
import com.zjgsu.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.Query;

import com.zjgsu.gulimall.ware.dao.PurchaseDao;
import com.zjgsu.gulimall.ware.entity.PurchaseEntity;
import com.zjgsu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0)
                        .or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);

            purchaseId=purchaseEntity.getId();

        }
        //TODO 确认采购单状态是 0/1
        //FINISHED
        PurchaseEntity byId = this.getById(purchaseId);
        Integer status = byId.getStatus();
        if (status==WareConstant.PurchaseStatusEnum.CREATED.getCode()||status==WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()){
            List<Long> items = mergeVo.getItems();
            Long finalPurchaseId = purchaseId;
            List<PurchaseDetailEntity> collect = items.stream().map(i -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(i);
                purchaseDetailEntity.setPurchaseId(finalPurchaseId);
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());

                return purchaseDetailEntity;
            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(collect);

            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.updateById(purchaseEntity);
        }


    }

    @Override
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);

        //3、改变采购项的状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> purchaseDetailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(entity.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());

                return purchaseDetailEntity;
            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        });

    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {



        //2、改变采购项的状态 flag是为了给采购单作标注是否采购完成的
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();

        List<PurchaseDetailEntity> updates = new ArrayList<PurchaseDetailEntity>();
        Boolean flag=true;
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatus()==WareConstant.PurchaseStatusEnum.HASERROR.getCode()){
                purchaseDetailEntity.setStatus(item.getStatus());
                flag=false;
            }else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISH.getCode());
                //3、将成功采购的入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addTock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }

        purchaseDetailService.updateBatchById(updates);

        //1、改变采购单的状态
        Long id = purchaseDoneVo.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());

        this.updateById(purchaseEntity);




    }

}