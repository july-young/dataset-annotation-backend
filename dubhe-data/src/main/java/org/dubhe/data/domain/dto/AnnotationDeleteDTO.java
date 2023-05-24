

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 删除标注

 */
@Data
public class AnnotationDeleteDTO implements Serializable {

    @ApiModelProperty(value = "datasetIds", required = true)
    @NotNull(message = "数据集id不能为空")
    private Long datasetId;

}
