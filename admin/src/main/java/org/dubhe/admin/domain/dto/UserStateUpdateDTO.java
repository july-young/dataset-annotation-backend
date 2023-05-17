
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 批量修改用户组用户状态DTO
 * @date 2021-05-12
 */
@Data
public class UserStateUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value ="用户组id" )
    @NotNull(message = "用户组id不能为空")
    private Long groupId;

    @ApiModelProperty("用户状态：激活：true，锁定：false")
    private boolean enabled;
}
