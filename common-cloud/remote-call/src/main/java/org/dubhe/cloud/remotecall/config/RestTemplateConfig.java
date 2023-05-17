
package org.dubhe.cloud.remotecall.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.dubhe.cloud.remotecall.interceptor.RestTemplateTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @description RestTemplate配置类
 * @date 2020-11-26
 */
@Configuration
@ConditionalOnMissingBean(RestTemplate.class)
public class RestTemplateConfig {

    @Autowired
    private CloseableHttpClient httpClient;

    /**
     * 负载均衡
     * @return RestTemplate
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory(httpClient)
        );
        //拦截器统一添加token
        restTemplate.setInterceptors(
                Collections.singletonList(
                        new RestTemplateTokenInterceptor()
                )
        );
        return restTemplate;
    }

}
