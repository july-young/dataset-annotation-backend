

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 普通数据集转为预置数据集请求实体
 * @date 2021-03-10
 */
@Data
public class DatasetConvertPresetDTO implements Serializable {

    @ApiModelProperty(value = "datasetId", required = true)
    @NotNull(message = "数据集id不能为空")
    private Long datasetId;


    @ApiModelProperty(value = "versionName", required = true)
    @NotNull(message = "版本名称")
    private String versionName;


    @ApiModelProperty(value = "name", required = true)
    @NotNull(message = "数据集名称")
    private String name;

}
