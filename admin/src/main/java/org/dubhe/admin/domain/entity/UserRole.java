package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 用户角色关系实体

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("users_roles")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -6296866205797727963L;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "role_id")
    private Long roleId;


}
