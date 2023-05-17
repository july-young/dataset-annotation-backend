package org.dubhe.biz.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @description 用户信息
 * @date 2020-06-29
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    private String username;

    private String nickName;

    private String sex;

    private String email;

    private String phone;

    private Boolean enabled;

    private String remark;

    private Date lastPasswordResetTime;

    private Timestamp createTime;

    /**
     * 头像路径
     */
    private String userAvatarPath;

    /**
     * 角色
     */
    private List<SysRoleDTO> roles;

}
