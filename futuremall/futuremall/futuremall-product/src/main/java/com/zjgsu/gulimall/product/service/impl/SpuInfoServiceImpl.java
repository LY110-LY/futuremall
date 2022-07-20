package com.zjgsu.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.zjgsu.common.constant.ProductConstant;
import com.zjgsu.common.to.SkuHasStockVo;
import com.zjgsu.common.to.es.SkuEsModel;
import com.zjgsu.common.to.SkuReductinTo;
import com.zjgsu.common.to.SpuBoundTo;
import com.zjgsu.common.utils.R;
import com.zjgsu.gulimall.product.entity.*;
import com.zjgsu.gulimall.product.feign.CouponFeignService;
import com.zjgsu.gulimall.product.feign.SearchFeignService;
import com.zjgsu.gulimall.product.feign.WareFeignService;
import com.zjgsu.gulimall.product.service.*;
import com.zjgsu.gulimall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjgsu.common.utils.PageUtils;
import com.zjgsu.common.utils.Query;

import com.zjgsu.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;


    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //TODO 高级部分进行完善，保存失败的一些事务回滚
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1、保存spu基本信息：pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        //2、保存Spu的描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        //3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);

        //4、保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        productAttrValueService.saveProdutAttr(baseAttrs);

        //5、保存spu的积分信息：跨库：gulimall_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode()!=0){
            log.error("远程保存spu积分信息失败");
        }

        //6、保存当前spu对应的所有sku信息：
        List<Skus> skus = vo.getSkus();
        if (skus!=null&&skus.size()!=0){
            skus.forEach(item->{
                String defaultImg="";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg()==1){
                        defaultImg=image.getImgUrl();
                    }
                }
//                private String skuName;
//                private BigDecimal price;
//                private String skuTitle;
//                private String skuSubtitle;
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //6.1)、sku的基本信息：pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                //自增主键
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());

                    return skuImagesEntity;
                }).filter(entity->{
                    //返回true就是需要保存，返回false就是过滤掉
                    return StringUtils.hasLength(entity.getImgUrl());
                }).collect(Collectors.toList());

                //6.2)、sku的图片信息：pms_sku_images
                //TODO 没有图片路径的无需保存至库
                skuImagesService.saveBatch(imagesEntities);


                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                //6.3)、sku的销售属性信息：pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4)、sku的优惠、满减信息：gulimall_sms->sms_sku_ladder->sms_sku_full_reduction\sms_member_price
                SkuReductinTo skuReductinTo = new SkuReductinTo();
                BeanUtils.copyProperties(item,skuReductinTo);


                List<MemberPrice> memberPrices = new ArrayList<MemberPrice>();
                List<com.zjgsu.common.to.MemberPrice> memberPriceList = memberPrices.stream().map(member -> {
                    com.zjgsu.common.to.MemberPrice memberPrice = new com.zjgsu.common.to.MemberPrice();
                    memberPrice.setId(member.getId());
                    memberPrice.setName(member.getName());
                    memberPrice.setPrice(member.getPrice());

                    return memberPrice;
                }).collect(Collectors.toList());
                skuReductinTo.setMemberPrice(memberPriceList);


                skuReductinTo.setSkuId(skuId);
                if (skuReductinTo.getFullCount()>=0||skuReductinTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductinTo);
                    if (r1.getCode()!=0){
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }



    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.hasLength(key)){
            wrapper.and(w->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if (StringUtils.hasLength(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.hasLength(status) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.hasLength(status)&& !"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper

        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {


        //1、查出当前spuid对应的所有sku信息，包括品牌的名字等
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        //TODO 4、查出当前sku的所有可以被检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrListforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);

        Set<Long> idSet = new HashSet<>(searchAttrIds);


        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        //TODO 1、发送远程调用，库存系统查询是否有库存
        //这里每次查询，有多少个sku，就要查多少次，太麻烦
        //所以我们希望远程ware服务，能提供一个方法。直接查到所有sku是否有库存
        Map<Long, Boolean> stockMap=null;
        try {
            R skuHasStock = wareFeignService.getSkuHasStock(skuIdList);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            List<SkuHasStockVo> data = (List<SkuHasStockVo>) skuHasStock.getData(typeReference);
            stockMap = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e){
            log.error("库存服务查询异常：原因{}",e);
        }


        //2、封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProduct = skus.stream().map(sku -> {
            //组装需要的数据
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,esModel);
            //对属性名不一样的或者属性不存在的属性进行单独操作
            //skuPrice,skuImg
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            //hasStock,hotScore
            //设置库存信息
            if (finalStockMap ==null){
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }


            //TODO 2、热度评分。目前先默认置 0
            esModel.setHotScore(0L);

            //TODO 3、查询品牌和分类的名字的信息
            BrandEntity brand = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());

            CategoryEntity category = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(category.getName());

            //设置检索属性
            esModel.setAttrs(attrsList);



            return esModel;
        }).collect(Collectors.toList());

        //TODO 5、将数据发给es进行保存；gulimall-search
        R r = searchFeignService.productStatusUp(upProduct);
        if (r.getCode()==0){
            //远程调用成功
            //TODO 6、修改当前spu的状态
            this.baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else {
            //远程调用失败
            //TODO 7、考虑一个重复调用的问题？专业术语：接口幂等性；重试机制？
            //Feign调用流程
            /**
             * 1、构造请求数据，将对象转为json；
             *      RequestTemplate template = this.buildTemplateFromArgs.create(argv);
             * 2、发送请求进行执行(执行成功会解码响应数据)：
             *      executeAndDecode(template, options);
             * 3、执行请求会有重试机制
             *      while(true){
             *          try(){
             *              executeAndDecode(twmplate);
             *          }catch(){
             *              try{retryer.continueOrPropagate(e);}catch(){throw ex;}
             *              continue;
             *          }
             *      }
             */
        }

    }

}