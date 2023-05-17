
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 修改权限组DTO
 * @date 2021-05-14
 */
@Data
public class AuthCodeUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "autoCode", required = true)
    @NotEmpty(message = "权限组code不能为空")
    private String authCode;

    @ApiModelProperty(value = "权限id集合")
    private Set<Long> permissions;

    @ApiModelProperty("描述")
    private String description;

}
