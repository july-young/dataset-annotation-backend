package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.entity.BaseEntity;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @description 用户实体

 */
@Data
@TableName("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3836401769559845765L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "username")
    private String username;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "enabled")
    private Boolean enabled;

    @TableField(value = "password")
    private String password;

    @TableField(value = "last_password_reset_time")
    private Date lastPasswordResetTime;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "avatar_id")
    private Long avatarId;

    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted = false;

    @TableField(exist = false)
    private UserAvatar userAvatar;


    @NotEmpty
    @TableField(exist = false)
    private List<Role> roles;

}
