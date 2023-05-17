
package org.dubhe.biz.base.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @description 公共权限信息DTO
 * @date 2020-11-25
 */
@Data
@Builder
public class CommonPermissionDataDTO implements Serializable {

    /**
     * 资源拥有者ID
     */
    private Long id;

    /**
     * 公共类型
     */
    private Boolean type;

    /**
     *  资源所属用户ids
     */
    private Set<Long> resourceUserIds;

}
