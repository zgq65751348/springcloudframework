package org.github.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @ClassName: RateLimiterConfiguration
 * @Description:
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-08 21:45
 * @Copyright: 墨铭琦妙代码研究中心
 */

@Configuration
public class RateLimiterConfiguration {

    @Bean(value = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}

