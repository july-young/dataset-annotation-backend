

package org.dubhe.biz.base.vo;

import lombok.Data;
import org.dubhe.biz.base.dto.UserSmallDTO;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 数据集VO

 */
@Data
public class DatasetVO implements Serializable {

    /**
     * 数据集ID
     */
    private Long id;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 数据集文件主目录
     */
    private String uri;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 标注类型
     */
    private Integer annotateType;

    /**
     * 数据集状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 创建人
     */
    private UserSmallDTO createUser;

    /**
     * 更新人
     */
    private UserSmallDTO updateUser;

    /**
     * 进度
     */
    private ProgressVO progress;

    /**
     * 当前版本
     */
    private String currentVersionName;

    /**
     * 是否导入
     */
    private boolean isImport;

    /**
     * 解压状态
     */
    private Integer decompressState;

    /**
     * 是否置顶
     */
    private boolean isTop;

    /**
     * 标签组ID
     */
    private Long labelGroupId;

    /**
     * 标签组名称
     */
    private String labelGroupName;

    /**
     * 标签组类型
     */
    private Integer labelGroupType;

    /**
     * 是否自动标注
     */
    private boolean autoAnnotation;

    /**
     * 数据转换状态
     */
    private Integer dataConversion;

    /**
     * 源ID
     */
    private Long sourceId;

    /**
     * 文件数量
     */
    private Integer fileCount;

    /**
     * 模板
     */
    private Integer templateType;

    /**
     * 所属模块
     */
    private Integer module;

}
