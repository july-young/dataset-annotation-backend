
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 操作权限DTO
 * @date 2021-04-29
 */
@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "权限id")
    private Long id;

    @ApiModelProperty(value = "父权限id")
    private Long pid;

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty(value = "权限名称")
    private String name;
}
