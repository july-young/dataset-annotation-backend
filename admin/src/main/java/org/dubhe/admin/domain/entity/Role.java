package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.entity.BaseEntity;

import java.io.Serializable;
import java.util.Set;

/**
 * @description 角色实体

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -812009584744832371L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    /**
     * 权限
     */
    @TableField(value = "permission")
    private String permission;

    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private Set<Menu> menus;

    @TableField(exist = false)
    private Set<Auth> auths;

    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Boolean deleted = false;


    public @interface Update {
    }
}
