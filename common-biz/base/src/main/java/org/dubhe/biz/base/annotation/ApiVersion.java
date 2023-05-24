

package org.dubhe.biz.base.annotation;

import java.lang.annotation.*;

/**
 * @description API版本控制注解

 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {
  //标识版本号
  int value() default  1;
}
