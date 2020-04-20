package com.fkzm.rpcconsumer.controller;

import com.fkzm.rpcconsumer.feign.ProviderLocalFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dejavu
 * @create 2020/04/17
 */
@RefreshScope
@RestController()
@RequestMapping("redis_consumer")
public class TestRoute {
    @Autowired
    ProviderLocalFeignClient providerLocalFeignClient;

    @GetMapping("/test/abc")
    public String test() {
        return "ok";
    }

    @GetMapping("/testFeign")
    public String testFeign() {
        return providerLocalFeignClient.test()+","+providerLocalFeignClient.test1();
    }
}
