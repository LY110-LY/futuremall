package com.zjgsu.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、整合Mybatis-Plus
     * 1、导入依赖
     * 2、配置
     *  1）配置数据源
     *      1）、导入数据库的驱动
     *      2）、在application.yml配置数据源相关信息
     *  2）配置Mybatis-Plus：
     *      1）、使用@MapperScan
     *      2）、告诉Mybatis-Plus，sql映射文件的位置
 * 2、逻辑删除
 *  1）配置全局的逻辑删除规则（高版本Mybatis-Plus可省略）
 *  2）配置逻辑删除的组件Bean（高版本Mybatis-Plus可省略）
 *  3）给实体类Bean字段加上逻辑删除注解@TableLogic（最重要）
 * 3、JSR303
 *  1）给Bean添加校验注解：javax.validation.constraints，并定义自己的message提示
 *  2）开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应
 *  3）给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *  4）分组校验（多场景的复杂校验）
 *      1）、@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *      给校验注解标注，什么情况需要进行校验
 *      2）、@Validated({AddGroup.class})
 *      3）、默认没有指定分组的校验注解@NatBlank在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效
 *  5）自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器
 *      3）、关联自定义的校验器和自定义的校验注解
 *      @Documented
 * @Constraint(
 *         validatedBy = {ListValueConstraintValidator.class}【可以指定多个不同的校验器，适配不同类型的校验】
 * )
 * @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface ListValue {
 * 4、很多属性都要使用数据校验，重复的代码，我们使用一个统一的异常处理
 * @ControllerAdvice
 *
 * 7、整合redisson作为分布式锁等功能框架
 *      1）、引入依赖
 *      2）、配置redisson
 *          MyRedissonConfig给容器中配置一个RedissonClient实例即可
 *      3）、使用
 *          参照文档即可
 * 8、整合SpringCache简化缓存开发
 *      1）、引入依赖
 *      2）、写配置
 *          （1）、自动配置了哪些
 *              CacheAutoConfiguration会导入RedisCacheConfiguration
 *              自动配好了缓存管理器RedisCacheManager
 *           (2)、配置使用redis作为缓存
 *              spring.cache.type=redis
 *      3）、测试使用缓存
 *          @Cacheable:触发将数据保存到缓存的操作
 *          @CacheEvict:触发将数据从缓存删除的操作
 *          @CachePut:不影响方法的同时执行更新缓存
 *          @Caching:组合以上多个操作
 *          @CacheConfig:在类级别共享缓存的相同配置
 *          1）、开启缓存功能 @EnableCaching
 *          2）、只需要使用注解就能完成缓存操作
 *
 *
 */

//开启远程调用功能
@EnableFeignClients(basePackages = "com.zjgsu.gulimall.product.feign")
//开启服务注册与发现功能
@EnableDiscoveryClient
@MapperScan("com.zjgsu.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
