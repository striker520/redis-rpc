package com.fkzm.rpcprovider.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

/**
 * @author dejavu
 * @create 2020/04/17
 */

public class DefaultFeignConfig {

    @Bean
    public Retryer feignRetryer() {

        return new Retryer.Default(1000, 3000, 3);
    }
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }


}
