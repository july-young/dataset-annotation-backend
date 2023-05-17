package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 用户组成员操作DTO
 * @date 2021-05-11
 */
@Data
public class UserGroupUpdDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户组id")
    @NotNull(message = "用户组id不能为空")
    private Long groupId;

    @ApiModelProperty(value = "用户id集合")
    private Set<Long> userIds;

    @ApiModelProperty(value = "角色id集合")
    private Set<Long> roleIds;
}
