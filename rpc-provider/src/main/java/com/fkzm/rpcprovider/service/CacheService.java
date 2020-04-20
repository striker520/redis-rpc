package com.fkzm.rpcprovider.service;

import com.fkzm.rpcprovider.entity.R;
import org.springframework.stereotype.Service;

/**
 * @author dejavu
 * @create 2020/04/17
 */
@Service
public interface CacheService {
    //name=JDNI name(即 BeanId)


    R miaosha(Integer count);
}
