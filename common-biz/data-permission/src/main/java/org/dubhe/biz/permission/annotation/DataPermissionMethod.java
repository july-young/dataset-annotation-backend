
package org.dubhe.biz.permission.annotation;


import org.dubhe.biz.base.enums.DatasetTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 数据权限方法注解

 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermissionMethod {

    /**
     * 是否需要拦截标识 true: 不拦截 false: 拦截
     *
     * @return 拦截标识
     */
    boolean interceptFlag() default false;

    /**
     * 数据类型
     *
     * @return 数据集类型
     */
    DatasetTypeEnum dataType() default DatasetTypeEnum.PRIVATE;
}
