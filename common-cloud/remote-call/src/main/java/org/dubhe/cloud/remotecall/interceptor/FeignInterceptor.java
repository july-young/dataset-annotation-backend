
package org.dubhe.cloud.remotecall.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.dubhe.cloud.remotecall.config.RemoteCallConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description Feign请求拦截器

 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 拦截feign请求，传递token
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (RequestContextHolder.getRequestAttributes() == null){
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        RemoteCallConfig.TOKEN_LIST.forEach(token -> {
            requestTemplate.header(token, request.getHeader(token));
        });
    }
}
