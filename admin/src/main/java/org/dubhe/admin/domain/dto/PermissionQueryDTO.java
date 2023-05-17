
package org.dubhe.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 权限查询DTO
 * @date 2021-05-31
 */
@Data
public class PermissionQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyword;
}
