
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dubhe.admin.domain.entity.Permission;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @description 修改操作权限DTO
 * @date 2021-04-28
 */
@Data
public class AuthUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "操作权限名称")
    @Size(max = 255, message = "名称长度不能超过255")
    private String name;

    @ApiModelProperty(value = "操作权限标识")
    @Size(max = 255, message = "默认权限长度不能超过255")
    private String permission;


    private Set<Permission> permissions;

    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthUpdateDTO role = (AuthUpdateDTO) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public @interface Update {
    }
}
