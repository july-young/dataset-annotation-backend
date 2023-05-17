

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 删除数据集
 * @date 2020-07-09
 */
@Data
public class DatasetDeleteDTO implements Serializable {

    @ApiModelProperty(value = "ids", required = true)
    @NotNull(message = "id不能为空")
    private Long[] ids;

}
