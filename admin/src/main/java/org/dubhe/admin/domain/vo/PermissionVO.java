
package org.dubhe.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 权限VO
 * @date 2021-05-31
 */
@Data
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long pid;

    private String permission;

    private String name;

    private Timestamp createTime;

    private Timestamp updateTime;

    private List<PermissionVO> children;
}
