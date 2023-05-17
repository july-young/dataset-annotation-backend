
package org.dubhe.admin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 资源规格查询结果封装类
 * @date 2021-05-27
 */
@Data
@Accessors(chain = true)
public class ResourceSpecsQueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("规格名称")
    private String specsName;

    @ApiModelProperty("规格类型(0为CPU, 1为GPU)")
    private Boolean resourcesPoolType;

    @ApiModelProperty("所属业务场景")
    private Integer module;

    @ApiModelProperty("CPU数量,单位：核")
    private Integer cpuNum;

    @ApiModelProperty("GPU数量，单位：核")
    private Integer gpuNum;

    @ApiModelProperty("内存大小，单位：Mi")
    private Integer memNum;

    @ApiModelProperty("工作空间的存储配额，单位：Mi")
    private Integer workspaceRequest;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("更新人")
    private Long updateUserId;

    @ApiModelProperty("更新时间")
    private Timestamp updateTime;
}