

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description 数据集版本文件 Mapper 接口
 * @date 2020-7-15
 */
@Data
public class DatasetIsVersionDTO {

    @ApiModelProperty(value = "数据集ID")
    private List<Long> ids;

    @ApiModelProperty(value = "数据标注类型 图像分类 目标检测 目标跟踪")
    private String annotateType;

    @ApiModelProperty(value = "所属模块")
    private Integer module;

}
