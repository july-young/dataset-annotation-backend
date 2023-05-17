

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 普通标签组转为预置标签组请求实体
 * @date 2021-03-10
 */
@Data
public class GroupConvertPresetDTO implements Serializable {

    @ApiModelProperty(value = "labelGroupId", required = true)
    @NotNull(message = "标签组id不能为空")
    private Long labelGroupId;


}
