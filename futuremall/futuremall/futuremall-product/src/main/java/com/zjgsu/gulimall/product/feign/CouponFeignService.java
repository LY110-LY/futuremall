package com.zjgsu.gulimall.product.feign;

import com.zjgsu.common.to.SkuReductinTo;
import com.zjgsu.common.to.SpuBoundTo;
import com.zjgsu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//声明调用哪个远程服务
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * SpringCloud远程调用的逻辑
     * 1、CouponFeignService.saveSpuBounds(spuBoundTo)
     *      1）、@RequestBody将传入的对象转为json。
     *      2）、Spring Cloud会给远程服务发送/coupon/spubounds/save这个请求，去注册中心中找到gulimall-coupon这个远程服务，
     *          再给服务/coupon/spubounds/save发送请求。将上一步转的json放在请求体位置，发送请求
     *      3）、对方服务收到请求。请求体里有json数据。
     *          (@RequestBody SpuBoundsEntity spuBounds):将请求体的json转为SpuBoundsEntity
     * 总结：只要json数据是兼容的，双方服务无需使用同一个to
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductinTo skuReductinTo);
}
