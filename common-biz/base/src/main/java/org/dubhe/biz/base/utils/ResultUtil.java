

package org.dubhe.biz.base.utils;

import cn.hutool.core.util.StrUtil;
import org.dubhe.biz.base.exception.BusinessException;

/**
 * 调用结果处理工具类
 *
 */
public class ResultUtil {
    /**
     * 判断调用结果非空
     *
     * @param object
     * @param errorMessageTemplate
     * @param params
     */
    public static void notNull(Object object, String errorMessageTemplate, Object... params) {
        if (object == null) {
            throw new BusinessException(StrUtil.format(errorMessageTemplate, params));
        }
    }

    /**
     * 判断调用结果相等
     *
     * @param object1
     * @param object2
     * @param errorMessageTemplate
     * @param params
     */
    public static void isEquals(Object object1, Object object2, String errorMessageTemplate, Object... params) {
        if(object1 == null) {
            if (object2 == null) {
                return;
            }
            throw new BusinessException(String.format(errorMessageTemplate, params));
        }
        if (!object1.equals(object2)) {
            throw new BusinessException(String.format(errorMessageTemplate, params));
        }
    }

    public static void isTrue(Boolean object, String errorMessageTemplate, Object... params) {
        if (!Boolean.TRUE.equals(object)) {
            throw new BusinessException(String.format(errorMessageTemplate, params));
        }
    }
}