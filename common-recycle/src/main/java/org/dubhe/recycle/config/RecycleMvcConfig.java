

package org.dubhe.recycle.config;


import org.dubhe.recycle.interceptor.RecycleCallInterceptor;
import org.dubhe.recycle.utils.RecycleTool;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description 资源回收 Mvc Config
 * @date 2021-01-21
 */
@Configuration
public class RecycleMvcConfig implements WebMvcConfigurer {

    @Resource
    private RecycleCallInterceptor recycleCallInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(recycleCallInterceptor);
        // 拦截配置
        registration.addPathPatterns(RecycleTool.MATCH_RECYCLE_PATH);
    }

}
