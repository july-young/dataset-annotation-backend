package org.dubhe.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 用户VO

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = -8697100416579599857L;

    /**
     * 账号
     */
    private String username;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 密码 ： 账号 + md5
     */
    private String password;

    /**
     * 是否有管理员权限
     */
    private Boolean is_staff;

}
