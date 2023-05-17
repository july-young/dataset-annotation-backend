
package org.dubhe.admin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description
 * @date 2021-05-17
 */
@Data
public class RoleAuthUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @NotNull(message = "权限组id不能为空")
    private Set<Long> authIds;
}
