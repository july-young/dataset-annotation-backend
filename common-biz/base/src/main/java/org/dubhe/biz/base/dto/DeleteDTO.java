package org.dubhe.biz.base.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 删除权限组DTO
 * @date 2021-05-17
 */
@Data
public class DeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    private Set<Long> ids;
}
