

package org.dubhe.biz.permission.config;

import org.dubhe.biz.permission.interceptor.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description mybatis plus拦截器
 * @date 2020-11-26
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 注入 MybatisPlus 分页拦截器
     *
     * @return  自定义MybatisPlus分页拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}

