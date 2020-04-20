package com.fkzm.rpcconsumer.feign;

import com.fkzm.rpcconsumer.config.DefaultFeignConfig;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author dejavu
 * @create 2020/04/17
 */

@FeignClient(name="redis-rpc-provider",url = "http://127.0.0.1:88/api/redis_provider",configuration = {DefaultFeignConfig.class})
public interface ProviderLocalFeignClient {

    @RequestLine("GET       /test/abc")
     String test();
    @RequestLine("GET /test/abc/def")
     String test1();
}
