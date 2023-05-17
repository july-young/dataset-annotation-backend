
package org.dubhe.admin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 删除用户组DTO
 * @date 2021-05-11
 */
@Data
public class UserGroupDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户组id不能为空")
    private Set<Long> ids;
}
