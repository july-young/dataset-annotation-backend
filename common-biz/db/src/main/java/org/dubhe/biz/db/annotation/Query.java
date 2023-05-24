package org.dubhe.biz.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description  构建Wrapper的注解

 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {


    String propName() default "";

    Type type() default Type.EQ;


    String blurry() default "";

    enum Type {
        // 相等
        EQ
        // 不等于
        , NE
        // 大于
        , GT
        // 大于等于
        , GE
        // 小于
        , LT
        // 小于等于
        , LE,
        BETWEEN,
        NOT_BETWEEN,
        LIKE,
        NOT_LIKE,
        LIkE_LEFT,
        LIKE_RIGHT,
        IS_NULL,
        IS_NOT_NULL,
        IN,
        NOT_IN,
        INSQL,
        NOT_INSQL,
        ORDER_BY
    }

}

