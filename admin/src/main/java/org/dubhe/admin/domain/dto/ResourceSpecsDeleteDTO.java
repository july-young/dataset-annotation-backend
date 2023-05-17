
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @description 资源规格删除
 * @date 2021-05-27
 */
@Data
@Accessors(chain = true)
public class ResourceSpecsDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Set<Long> ids;
}