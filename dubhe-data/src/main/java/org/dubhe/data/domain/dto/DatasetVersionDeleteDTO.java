

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dubhe.biz.base.constant.NumberConstant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 删除数据集版本参数

 */
@Data
public class DatasetVersionDeleteDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    @Min(value = NumberConstant.NUMBER_0, message = "数据集ID不能小于0")
    private Long datasetId;

    @ApiModelProperty(value = "versionName", required = true)
    @NotNull(message = "versionName不能为空")
    private String versionName;

}