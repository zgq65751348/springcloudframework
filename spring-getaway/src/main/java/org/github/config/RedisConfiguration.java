package org.github.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName: RedisConfiguration
 * @Description:
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-08 21:43
 * @Copyright: 墨铭琦妙代码研究中心
 */

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(@Value("${spring.redis.host}") String host,
                                       @Value("${spring.redis.port}") int port) {
        RedisTemplate redisTemplate = new RedisTemplate();
        RedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(standaloneConnectionFactory(host, port));
        return redisTemplate;
    }

    protected JedisConnectionFactory standaloneConnectionFactory(String host, int port) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
}

