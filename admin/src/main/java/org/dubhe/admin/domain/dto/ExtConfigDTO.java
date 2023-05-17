

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @description 扩展配置DTO
 * @date 2021-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtConfigDTO implements Serializable {

    @ApiModelProperty(value = "返回上一级菜单")
    private String backTo;
}
