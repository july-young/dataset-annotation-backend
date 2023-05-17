
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 * @date 2021-05-17
 */
@Data
@TableName("roles_auth")
public class RoleAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "role_id")
    private Long roleId;

    @TableField(value = "auth_id")
    private Long authId;
}
