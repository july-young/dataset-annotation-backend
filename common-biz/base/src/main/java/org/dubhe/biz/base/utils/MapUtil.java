


package org.dubhe.biz.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * @description  Map工具类
 * @date 2022-05-16
 */
public class MapUtil {
    public static Map<String, String> convertJsonObject(JSONObject object) {
        if (object == null) {
            return new HashMap<>();
        }
        return JSONObject.parseObject(object.toJSONString(),
                new TypeReference<Map<String, String>>(){});
    }
}
