

package org.dubhe.cloud.authconfig.decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @description 线程池设置ThreadLocal用户信息
 * @date 2021-06-10
 */
@Slf4j
public class AuthenticationThreadLocalTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return () -> {
            try {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                runnable.run();
            } catch (Throwable e){
                log.error(e.getMessage(), e);
            } finally {
                try {
                    SecurityContextHolder.getContext().setAuthentication(null);
                } catch (Exception e){
                    log.error(e.getMessage(), e);
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
