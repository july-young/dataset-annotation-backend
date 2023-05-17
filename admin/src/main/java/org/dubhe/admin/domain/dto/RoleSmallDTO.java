

package org.dubhe.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 角色的实体转换
 * @date 2020-06-01
 */
@Data
public class RoleSmallDTO implements Serializable {

    private static final long serialVersionUID = -6601893040730122984L;
    private Long id;

    private String name;
}
