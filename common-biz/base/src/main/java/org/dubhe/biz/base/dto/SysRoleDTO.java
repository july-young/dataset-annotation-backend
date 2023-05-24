package org.dubhe.biz.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @description 系统角色DTO

 */
@Data
public class SysRoleDTO  implements Serializable {

    private static final long serialVersionUID = -3836401769559845765L;

    /**
     * 权限列表
     */
    private List<SysPermissionDTO> permissions;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色ID
     */
    private Long id;

}
