package com.fkzm.rpcprovider.controller;

import com.fkzm.rpcprovider.entity.R;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController()
@RequestMapping("redis_provider")
public class TestRoute {

    @GetMapping("/test/abc")
    public R  test(){
        return R.ok();
    }
    @GetMapping("/test/abc/def")
    public R test1(){
        return R.error();
    }
}

