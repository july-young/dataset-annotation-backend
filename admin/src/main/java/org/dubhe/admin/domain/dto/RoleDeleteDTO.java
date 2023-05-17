

package org.dubhe.admin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 角色删除 dto
 * @date 2020-06-29
 */
@Data
public class RoleDeleteDTO implements Serializable {

    private static final long serialVersionUID = -6599428238298923816L;

    @NotEmpty(message = "角色id不能为空")
    private Set<Long> ids;

}
