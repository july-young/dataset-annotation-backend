

package org.dubhe.data.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description ES数据查询DTO
 * @date 2020-03-24
 */
@Data
public class EsDataFileDTO implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 数据集ID
     */
    private Long datasetId;

    /**
     * 创建用户
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新用户
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
     * 标签ID
     */
    private List<Long> labelIdList;

    /**
     * 标注信息
     */
    private String annotation;

    /**
     * 版本名称
     */
    private String versionName;
}
