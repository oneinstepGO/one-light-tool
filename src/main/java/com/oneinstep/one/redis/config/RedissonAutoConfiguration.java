package com.hst.bss.light.redis.configuration;

import com.hst.bss.light.redis.properties.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动注入 redis client
 *
 * @author aaron.shaw
 * @date 2023-05-24 15:07
 **/
@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnProperty(prefix = "bss.light.redisson", name = "enable", havingValue = "true")
@Slf4j
public class RedissonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Config config = new Config();
        if (1 == redissonProperties.getMode()) {
            config.useClusterServers()
                    .addNodeAddress("redis://" + redissonProperties.getNodeAddress())
                    .setUsername(redissonProperties.getUsername())
                    .setPassword(redissonProperties.getPassword())
                    .setMasterConnectionPoolSize(redissonProperties.getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redissonProperties.getSlaveConnectionPoolSize())
                    .setConnectTimeout(redissonProperties.getConnectTimeout());
            log.info("Create RedissonClient by bss-light-tool use useClusterServers.");
        } else {
            config.useSingleServer()
                    .setAddress(redissonProperties.getNodeAddress())
                    .setUsername(redissonProperties.getUsername())
                    .setPassword(redissonProperties.getPassword())
                    .setConnectionPoolSize(redissonProperties.getMasterConnectionPoolSize())
                    .setConnectTimeout(redissonProperties.getConnectTimeout())
                    .setDatabase(redissonProperties.getDatabase());
            log.info("Create RedissonClient by bss-light-tool use useSingleServer.");
        }
        return Redisson.create(config);
    }

}
