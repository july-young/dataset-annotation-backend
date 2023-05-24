package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @description 角色菜单关系实体

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("roles_menus")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = -6296866205797727963L;

    @TableField(value = "menu_id")
    private Long menuId;

    @TableField(value = "role_id")
    private Long roleId;


}
