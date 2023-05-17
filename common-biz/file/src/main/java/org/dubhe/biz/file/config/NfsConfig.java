

package org.dubhe.biz.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description NFS config
 * @date 2020-05-13
 */
@Data
@Component
public class NfsConfig {

    @Value("${storage.file-store}")
    private String nfsIp;

    @Value("${storage.file-store-root-path}")
    private String rootDir;

    @Value("/${minio.bucketName}/")
    private String bucket;

}
