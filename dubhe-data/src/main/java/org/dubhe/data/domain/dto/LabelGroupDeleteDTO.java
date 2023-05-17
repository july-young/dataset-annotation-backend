package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @description 删除标签组
 */
@Data
public class LabelGroupDeleteDTO implements Serializable {

    @ApiModelProperty(value = "ids", required = true)
    @NotNull(message = "id不能为空")
    private List<Long> ids;

}
