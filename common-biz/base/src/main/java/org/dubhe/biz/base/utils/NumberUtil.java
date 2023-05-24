

package org.dubhe.biz.base.utils;

import org.dubhe.biz.base.exception.BusinessException;

import java.util.regex.Pattern;

/**
 * @description 数字验证工具

 */
public class NumberUtil {

    private static final String REGEX = "^[0-9]*$";

    private NumberUtil() {

    }

    /**
     * 判断是否为数字格式不限制位数
     *
     * @param object 待校验参数
     */
    public static void isNumber(Object object) {
        if (!((Pattern.compile(REGEX)).matcher(String.valueOf(object)).matches())) {
            throw new BusinessException("parameter is incorrect");
        }
    }
}
