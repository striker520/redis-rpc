package com.fkzm.rpcprovider.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author dejavu
 * @create 2020/04/17
 */

@Configuration
@EnableCaching
@RefreshScope
public class CacheConfig {




    @Bean
    public RedisConnectionFactory jedisConnectionFactory(RedisClusterConfiguration config){
        return new JedisConnectionFactory(config);
    }


    @Bean
    public CacheManager cacheManager(@Qualifier("jedisConnectionFactory")RedisConnectionFactory  factory){
        RedisCacheWriter redisCacheWriter=RedisCacheWriter.lockingRedisCacheWriter(factory);


        return new RedisCacheManager(redisCacheWriter, RedisCacheConfiguration.defaultCacheConfig());
    }

    @Bean
    public RedisTemplate<String,Object> stringRedisTemplate(@Qualifier("jedisConnectionFactory")RedisConnectionFactory factory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> serializer=new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("jedisConnectionFactory")RedisConnectionFactory factory){
        RedisMessageListenerContainer container=new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.setTopicSerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        Executor executor= Executors.newSingleThreadExecutor();
        container.setTaskExecutor(executor);



        container.afterPropertiesSet();
        return  container;
    }
//    @Bean
//    JedisCluster jedisCluster(@Qualifier("clusterProperties") ClusterConfig.ClusterProperties clusterProperties){
//        JedisCluster jedisCluster=new JedisCluster(clusterProperties.getClusterNodes());
//
//
//    }
}
