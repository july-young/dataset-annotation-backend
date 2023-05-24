
package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 删除标签组

 */
@Data
public class LabelDeleteDTO implements Serializable {

    @ApiModelProperty(value = "datasetId", required = true)
    @NotNull(message = "数据集id不能为空")
    private Long datasetId;

    @ApiModelProperty(value = "labelId", required = true)
    @NotNull(message = "标签id不能为空")
    private Long labelId;

}
