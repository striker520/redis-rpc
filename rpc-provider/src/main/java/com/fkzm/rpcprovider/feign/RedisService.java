package com.fkzm.rpcprovider.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * @author dejavu
 * @create 2020/04/17
 */
@FeignClient(name="redis-rpc-consumer")
@Service
public interface RedisService {
}
