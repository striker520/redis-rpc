package com.fkzm.rpcconsumer.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author dejavu
 * @create 2020/04/17
 */


@Configuration
public class RedisTemplateConfig {
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory(){
//        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
//        return jedisConnectionFactory;
//    }
//    @Bean
//    public RedisTemplate<String,Object> redisTemplate(){
//        RedisTemplate<String,Object>redisTemplate=new RedisTemplate<>();
//        redisTemplate.setConnectionFactory();
//    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory){
        RedisMessageListenerContainer container=new RedisMessageListenerContainer();
       container.setConnectionFactory(factory);
       container.setTopicSerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        Executor executor= Executors.newSingleThreadExecutor();
       container.setTaskExecutor(executor);
       return  container;
    }
}
