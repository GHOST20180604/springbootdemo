package com.hanyc.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * redisson
 *
 * @author hanyc
 * @date 2024/12/31 13:30
 * @company: 
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        if (redisProperties.getCluster() != null && !redisProperties.getCluster().getNodes().isEmpty()) {
            RedisProperties.Cluster cluster = redisProperties.getCluster();
            List<String> nodes = cluster.getNodes();
            List<String> newNodes = new ArrayList<>();
            nodes.forEach(index -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));
            config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setPassword(redisProperties.getPassword());
        } else {
            String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
            config.useSingleServer().setAddress(address).setPassword(redisProperties.getPassword());
        }
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}


