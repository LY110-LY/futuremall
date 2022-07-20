package com.zjgsu.gulimall.ware.service.impl;

import com.zjgsu.common.utils.R;
import com.zjgsu.gulimall.ware.feign.ProductFeignService;
import com.zjgsu.gulimall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.Query;

import com.zjgsu.gulimall.ware.dao.WareSkuDao;
import com.zjgsu.gulimall.ware.entity.WareSkuEntity;
import com.zjgsu.gulimall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        /**
         *    wareId: 123,//仓库id
         *    skuId: 123//商品id
         */

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();

        String skuId = (String) params.get("skuId");
        if (StringUtils.hasLength(skuId)){
            wrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if (StringUtils.hasLength(wareId)){
            wrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addTock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断是否有这个库存记录，有的话是更新，没有的话是新增
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities==null || wareSkuEntities.size()==0){
            //新增
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字,如果失败，整个事务无需回滚
            //使用try catch避免这个冗余字段添加失败时，导致整个事务的回滚(自己catch异常)
            //TODO 还可以用什么方法让异常出现以后事务不回滚
            try {
                R info = productFeignService.info(skuId);
                Map<String,Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode()==0){
                    wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                }
            }catch (Exception e){

            }



            wareSkuDao.insert(wareSkuEntity);
        }else {
            //更新
            wareSkuDao.addTock(skuId, wareId, skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {

        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();

            //查询当前sku的总库存量
            //SELECT SUM(stock-stock_locked) FROM `wms_ware_sku` WHERE sku_id=1
            Long count = this.baseMapper.getSkuStock(skuId);

            vo.setSkuId(skuId);
            vo.setHasStock(count==null?false:count>0);

            return vo;
        }).collect(Collectors.toList());

        return null;
    }

}