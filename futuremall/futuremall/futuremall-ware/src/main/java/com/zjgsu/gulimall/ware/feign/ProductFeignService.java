package com.zjgsu.gulimall.ware.feign;

import com.zjgsu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     *两种请求路径的写法
     *
     * 1）、让所有请求过网关：
     *      1、@FeignClient("gulimall-gateway")：给gulimall-gateway所在的服务器发请求
     *      2、请求路径：/api/product/skuinfo/info/{skuId}
     * 2）、直接让后台指定服务处理
     *      1、@FeignClient("gulimall-product")：给gulimall-product所在的服务器发请求
     *      2、请求路径：/product/skuinfo/info/{skuId}
     * @return
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
