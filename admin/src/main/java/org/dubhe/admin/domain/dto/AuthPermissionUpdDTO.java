
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 修改权限组权限DTO
 * @date 2021-05-14
 */
@Data
public class AuthPermissionUpdDTO implements Serializable {

    @ApiModelProperty(value = "权限组id")
    @NotNull(message = "权限组id不能为空")
    private Long authId;

    @ApiModelProperty(value = "权限id集合")
    private Set<Long> permissionIds;
}
