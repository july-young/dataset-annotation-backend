

package org.dubhe.data.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 数据集文件Bo
 * @date 2020-06-28
 */
@Data
public class DatasetFileBO implements Serializable {

    /**
     * 图片标注文件地址
     */
    private String annotationPath;

    /**
     * 图片地址
     */
    private String filePath;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件状态
     */
    private Integer annotationStatus;

    /**
     * 图片宽
     */
    private Integer width;

    /**
     * 图片高
     */
    private Integer height;

    public DatasetFileBO() {
    }

    public DatasetFileBO(String filePath, String annotationPath, Long fileId, Integer annotationStatus, Integer width, Integer height) {
        this.filePath = filePath;
        this.annotationPath = annotationPath;
        this.fileId = fileId;
        this.annotationStatus = annotationStatus;
        this.width = width;
        this.height = height;
    }

}
