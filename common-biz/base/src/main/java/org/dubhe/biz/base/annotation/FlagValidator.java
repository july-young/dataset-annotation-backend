
package org.dubhe.biz.base.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;

/**
 * @description 自定义状态校验注解(传入值是否在指定状态范围内)

 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FlagValidator.Validator.class)
@Documented
public @interface FlagValidator {

    String[] value() default {};

    String message() default "flag value is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @description 校验传入值是否在默认值范围校验逻辑

     */
    class Validator implements ConstraintValidator<FlagValidator, Integer> {

        private String[] values;

        @Override
        public void initialize(FlagValidator flagValidator) {
            this.values = flagValidator.value();
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null) {
                //当状态为空时，使用默认值
                return false;
            }
            return Arrays.stream(values).anyMatch(Integer.toString(value)::equals);
        }
    }
}
