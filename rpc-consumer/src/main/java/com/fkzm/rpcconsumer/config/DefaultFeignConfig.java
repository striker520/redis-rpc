package com.fkzm.rpcconsumer.config;


import feign.Contract;
import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

/**
 * @author dejavu
 * @create 2020/04/17
 */

public class DefaultFeignConfig {
    @Bean
    public Contract defaultContract(){
        return new feign.Contract.Default();
    }

    @Bean
    public Retryer feignRetryer() {

        Retryer.Default aDefault = new Retryer.Default(1000, 3000, 3);
        System.out.println("没有 @Configuration我也注入了,厉不厉害?");
        return aDefault;
    }
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }
    //使用 Feign 自己的注解契约,使上面的配置生效
    //@FeignClient 要加 @Configuration



}
