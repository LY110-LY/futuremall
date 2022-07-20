package com.zjgsu.gulimall.product;

import com.aliyun.oss.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjgsu.gulimall.product.entity.BrandEntity;
import com.zjgsu.gulimall.product.service.AttrService;
import com.zjgsu.gulimall.product.service.BrandService;
import com.zjgsu.gulimall.product.service.CategoryBrandRelationService;
import com.zjgsu.gulimall.product.service.CategoryService;
import com.zjgsu.gulimall.product.vo.AttrRespVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 使用对象存储的三步
 * 1、引入对象存储的starter：oss-starter
 * 2、配置key、endpiont相关信息
 * 3、使用OSSClient 进行相关操作
 */

@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void redisson(){
        System.out.println(redissonClient);
    }

    @Test
    public void teststringRedisTemplate(){
        // hello world
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        //保存
        ops.set("hello","world"+ UUID.randomUUID().toString());

        //查询
        String hello = ops.get("hello");
        System.out.println(hello);
    }


    @Test
    public void getBrandsByCatId(){

        List<BrandEntity> brandsByCatId = categoryBrandRelationService.getBrandsByCatId(225L);
    }

    @Test
    public void testLY(){
        AttrRespVo attrInfo = attrService.getAttrInfo(1L);
    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();

//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为");
//        brandService.updateById(brandEntity);

//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功...");

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",1L));
        list.forEach((item)->{
            System.out.println(item);
        });
    }

}
