
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dubhe.admin.domain.entity.Permission;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @description 新增权限DTO
 * @date 2021-05-31
 */
@Data
public class PermissionCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限父id")
    @NotNull(message = "父级权限id不能为空")
    private Long pid;

    @NotEmpty(message = "权限不能为空")
    private List<Permission> permissions;

}
