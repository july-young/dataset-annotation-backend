
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 批量修改用户组用户的角色
 * @date 2021-05-12
 */
@Data
public class UserRoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户组id")
    @NotNull(message = "用户组id不能为空")
    private Long groupId;

    @ApiModelProperty("角色id集合")
    @NotNull(message = "角色id不能为空")
    private Set<Long> roleIds;

}
