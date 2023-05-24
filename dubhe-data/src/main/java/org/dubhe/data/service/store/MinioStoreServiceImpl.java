

package org.dubhe.data.service.store;

import org.dubhe.biz.file.utils.MinioUtil;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @description minIo

 */
@Service
public class MinioStoreServiceImpl implements IStoreService {

    @Value("${minio.bucketName}")
    private String bucket;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * read
     *
     * @param filePath 文件路径
     * @return String 读取文件
     */
    @Override
    public String read(String filePath) {
        try {
            return minioUtil.readString(bucket, filePath);
        } catch (Exception e) {
            LogUtil.warn(LogEnum.BIZ_DATASET, "read annotation error.", e);
            return null;
        }
    }

    /**
     * write
     *
     * @param path    文件存储的全路径，包括文件名，非'/'开头. e.g. dataset/12/annotation/test.txt
     * @param content 需要存的文件内容
     * @return boolean 更新结果
     */
    @Override
    public boolean write(String path, Object content) {
        try {
            minioUtil.writeString(bucket, path, String.valueOf(content));
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "write file error.{}", e.toString());
            return false;
        }
        return true;
    }

    /**
     * delete
     *
     * @param fullFileOrDirPath 全文件路径
     * @return boolean 更新结果
     */
    @Override
    public boolean delete(String fullFileOrDirPath) {
        try {
            minioUtil.del(bucket, fullFileOrDirPath);
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "del file or dir error.", e);
            return false;
        }
        return true;
    }

}
