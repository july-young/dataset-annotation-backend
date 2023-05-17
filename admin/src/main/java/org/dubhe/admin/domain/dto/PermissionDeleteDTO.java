
package org.dubhe.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @description 删除权限DTO
 * @date 2021-05-31
 */
@Data
public class PermissionDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<Long> ids;
}
