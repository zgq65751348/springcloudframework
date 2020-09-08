package org.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: GetawayApplication
 * @Description:
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-08 21:42
 * @Copyright: 墨铭琦妙代码研究中心
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GetawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetawayApplication.class,args);
    }
}

