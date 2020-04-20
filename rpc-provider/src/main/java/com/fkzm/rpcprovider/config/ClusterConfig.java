package com.fkzm.rpcprovider.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dejavu
 * @create 2020/04/18
 */

@Configuration

public class ClusterConfig {
    @Bean
    ClusterProperties clusterProperties() {
        return new ClusterProperties();
    }

    @Bean
    RedisClusterConfiguration clusterConfiguration(@Qualifier("clusterProperties") ClusterProperties clusterProperties) {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        config.setClusterNodes(clusterProperties.getClusterNodes());
        config.setMaxRedirects(clusterProperties.getMaxRedirects());


        return config;
    }

    @RefreshScope
    @Validated
    @ConfigurationProperties(prefix = "spring.redis.cluster")
    static class ClusterProperties {
        @NotNull
        private List<String> nodes;
        private int maxRedirects;

        public List<RedisNode> getClusterNodes() {
            List<RedisNode> list = new ArrayList<>();
            for (String node : nodes) {
                String[] hp = node.split(":");
                RedisNode redisNode = new RedisNode(hp[0], Integer.parseInt(hp[1]));
                list.add(redisNode);
            }
            return list;
        }

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public int getMaxRedirects() {
            return maxRedirects;
        }

        public void setMaxRedirects(int maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }
}
