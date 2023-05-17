
package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description 数据集版本文件数据
 * @date 2020-07-09
 */
@Data
public class DatasetVersionFileDTO {

    private Long id;

    @ApiModelProperty("数据集ID")
    private Long datasetId;

    @ApiModelProperty("版本名称")
    private String versionName;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件状态 0:新增文件 1:删除文件 2:正常文件")
    private Integer status;

    @ApiModelProperty("标注状态")
    private Integer annotationStatus;

    @ApiModelProperty("备份状态，用于版本回退")
    private Integer backupStatus;

    @ApiModelProperty("更改标记，用于版本回退")
    private Integer changed;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("标签ID")
    private List<Long> labelIdList;

    @ApiModelProperty("预测值")
    private Double prediction;

}
