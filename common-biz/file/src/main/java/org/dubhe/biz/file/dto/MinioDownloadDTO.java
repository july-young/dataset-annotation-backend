

package org.dubhe.biz.file.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @description minio下载参数实体
 * @date 2021-06-24
 */
@Data
public class MinioDownloadDTO {
    /**
     * 下载压缩包请求token
     */
    private String token;
    /**
     * 下载压缩包请求参数
     */
    private String body;
    /**
     * 下载压缩包请求需要的header
     */
    private Map<String, Object> headers;
    /**
     * 下载压缩包文件名称
     */
    private String zipName;

    public MinioDownloadDTO() {
    }

    public MinioDownloadDTO(String token, String body, String zipName) {
        this.token = token;
        this.body = body;
        this.zipName = zipName;
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain;charset=UTF-8");
        this.headers = headers;
    }

}
