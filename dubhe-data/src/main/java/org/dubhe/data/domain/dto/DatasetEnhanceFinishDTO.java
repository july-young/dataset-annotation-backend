

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description 数据增强回调 DTO
 * @date 2020-06-29
 */
@Data
public class DatasetEnhanceFinishDTO implements Serializable {

    @ApiModelProperty("数据集id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty("增强类型后缀")
    @NotBlank(message = "id不能为空")
    private String suffix;

}
