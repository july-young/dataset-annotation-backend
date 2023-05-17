

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.admin.domain.entity.Menu;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 角色创建 DTO
 * @date 2020-06-29
 */
@Data
public class RoleCreateDTO implements Serializable {

    private static final long serialVersionUID = -8685787591892312697L;

    private Long id;

    @Size(max = 255, message = "名称长度不能超过255")
    private String name;

    /**
     * 权限
     */
    @Size(max = 255, message = "默认权限长度不能超过255")
    private String permission;

    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;

    private Set<Menu> menus;

    private Boolean deleted;


}
