
package org.dubhe.admin.service;

import java.util.Map;

/**
 * @description minIo服务 service
 * @date 2022-6-8
 **/
public interface MinIoService {
    /**
     * 对minio 的账户密码进行加密操作
     * @return  Map<String,String> minio账户密码加密map
     */
    Map<String,String> getMinIOInfo();
}
