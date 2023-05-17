
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 * @date 2021-05-14
 */
@Data
@TableName("auth_permission")
public class AuthPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 权限组id
     */
    @TableField(value = "auth_id")
    private Long authId;

    /**
     * 权限id
     */
    @TableField(value = "permission_id")
    private Long permissionId;
}
