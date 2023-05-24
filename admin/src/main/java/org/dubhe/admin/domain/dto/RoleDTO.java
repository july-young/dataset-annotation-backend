

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.admin.domain.vo.AuthVO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @description 角色的实体类

 */
@Data
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = -7250301719333643312L;
    private Long id;

    private String name;

    private String remark;

    private String permission;

    private Set<MenuDTO> menus;

    private Set<AuthVO> auths;

    private Timestamp createTime;
}
