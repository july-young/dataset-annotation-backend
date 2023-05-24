package org.dubhe.biz.base.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @description 系统权限DTO

 */
@Data
public class SysPermissionDTO  implements Serializable {

    private static final long serialVersionUID = -3836401769559845765L;

    private Long roleId;

    private String permission;

    private String name;

}
