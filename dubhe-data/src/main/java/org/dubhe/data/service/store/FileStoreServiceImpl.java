

package org.dubhe.data.service.store;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.stereotype.Service;

/**
 * @description 文件存储
 * @date 2020-05-09
 */
@Service
public class FileStoreServiceImpl implements IStoreService {

    /**
     * read
     *
     * @param path 文件路径
     * @return String 读取的文件
     */
    @Override
    public String read(String path) {
        try {
            return FileUtil.readUtf8String(path);
        } catch (IORuntimeException e) {
            return null;
        } catch (Exception e) {
            LogUtil.warn(LogEnum.BIZ_DATASET, "read annotation error.", e);
            return null;
        }
    }

    /**
     * write
     *
     * @param path 文件路径
     * @param content 文件目录
     * @return 更新结果
     */
    @Override
    public boolean write(String path, Object content) {
        FileUtil.writeUtf8String(String.valueOf(content), path);
        return true;
    }

    /**
     * delete
     *
     * @param fullFileOrDirPath 全文件路径
     * @return 更新结果
     */
    @Override
    public boolean delete(String fullFileOrDirPath) {
        return FileUtil.del(fullFileOrDirPath);
    }

}
