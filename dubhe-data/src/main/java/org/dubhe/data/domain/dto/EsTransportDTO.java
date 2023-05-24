

package org.dubhe.data.domain.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @description ES数据同步DTO

 */
@Data
public class EsTransportDTO {

    /**
     * 状态
     */
    private Integer annotationStatus;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件url
     */
    private String url;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 增强类型
     */
    private Integer enhanceType;

    /**
     * 用户ID
     */
    private Long originUserId;

    /**
     * 预测值
     */
    private Double prediction;

    /**
     * 文件ID
     */
    private Long id;

    /**
     * 标签ID
     */
    private Long[] labelId;

    /**
     * 标注信息
     */
    private String annotation;
}
