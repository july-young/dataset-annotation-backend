
package org.dubhe.biz.base.annotation;

/**
 * @description  数据权限注解
 * @date 2020-11-26
 */
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 只在类的注解上使用，代表方法的数据权限类型
     * @return
     */
    String permission() default "";

    /**
     * 不需要数据权限的方法名
     * @return
     */
    String[] ignoresMethod() default {};
}
