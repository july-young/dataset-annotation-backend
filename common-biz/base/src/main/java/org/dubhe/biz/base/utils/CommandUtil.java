

package org.dubhe.biz.base.utils;

import com.alibaba.fastjson.JSONObject;

import static org.dubhe.biz.base.constant.StringConstant.PYTHON_COMMAND_PATTERN;

/**
 * @description  命令行工具类
 * @date 2022-05-16
 */
public class CommandUtil {
    /**
     * 构造python运行命令
     *
     * @param runCommand
     * @param runParams
     * @return
     */
    public static String buildPythonCommand(String runCommand, JSONObject runParams) {
        StringBuilder sb = new StringBuilder();
        sb.append(runCommand);
        if (null != runParams && !runParams.isEmpty()) {
            runParams.forEach((k, v) -> sb.append(String.format(PYTHON_COMMAND_PATTERN, k, v)));
        }
        return sb.toString();
    }
}
