

package org.dubhe.data.util;

import com.alibaba.fastjson.JSON;
import org.dubhe.biz.base.utils.StringUtils;

/**
 * @description 校验字符串是否是 json 格式 工具类

 */
public class JsonUtil {

    /**
     * 校验字符串是否是json格式
     *
     * @param json json字符串
     * @return 校验结果 true:是 false:否
     */
    public static boolean isJson(String json) {
        boolean result = false;
        try {
            if(!StringUtils.isEmpty(json)){
                JSON.parse(json);
                result = true;
            }
        } catch (Exception e) {
            result=false;
        }
        return result;
    }

}

