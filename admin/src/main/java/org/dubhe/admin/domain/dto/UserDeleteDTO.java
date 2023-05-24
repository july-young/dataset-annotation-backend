

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 用户删除 dto

 */
@Data
public class UserDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id集合")
    @NotEmpty
    private Set<Long> ids;

}
