

package org.dubhe.biz.base.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 反射工具类

 **/
public class ReflectionUtils {

    /**
     * 获取所有字段集合
     *
     * @param clazz 反射对象
     * @return List<String> 字段集合
     **/
    public static List<String> getFieldNames(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNameList = new ArrayList<>();
        for (Field field : fields) {
            fieldNameList.add(field.getName());
        }
        return fieldNameList;
    }
}
