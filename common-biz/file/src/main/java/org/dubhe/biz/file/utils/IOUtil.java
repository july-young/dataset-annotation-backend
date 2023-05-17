
package org.dubhe.biz.file.utils;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;

import java.io.Closeable;
import java.io.IOException;

/**
 * @description IO流操作工具类
 * @date 2020-10-14
 */
public class IOUtil {

    /**
     * 循环的依次关闭流
     *
     * @param closeableList 要被关闭的流集合
     */
    public static void close(Closeable... closeableList) {
        for (Closeable closeable : closeableList) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                LogUtil.error(LogEnum.IO_UTIL, "关闭流异常，异常信息：{}", e);
            }
        }
    }
}
