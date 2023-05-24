

package org.dubhe.data.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import org.dubhe.biz.base.annotation.Log;
import org.dubhe.data.constant.Constant;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @description Zip工具

 */
public class ZipUtil {

    private static final Set<String> NEED_ZIP_DIR = new HashSet<String>() {{
        add(Constant.DATASET_ORIGIN_NAME);
        add(Constant.DATASET_ANNOTATION_NAME);
    }};

    /**
     * 压缩数据集文件夹中的标注、原始文件
     *
     * @param datasetDir  数据集文件夹
     * @return String 压缩数据集文件夹中的标注、原始文件路径
     */
    @Log
    public static String zip(String datasetDir) {
        if (datasetDir == null) {
            return null;
        }

        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".zip";
        File zip = new File(tempPath);
        cn.hutool.core.util.ZipUtil.zip(zip, CharsetUtil.defaultCharset(), true,
                (f) -> !f.isDirectory() || NumberUtil.isLong(f.getName()) || NEED_ZIP_DIR.contains(f.getName()),
                new File(datasetDir));
        return tempPath;
    }

}
