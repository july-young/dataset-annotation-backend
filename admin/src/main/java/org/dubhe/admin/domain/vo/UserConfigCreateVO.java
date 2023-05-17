
package org.dubhe.admin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description 用户配置创建返回 ID
 * @date 2021-7-2
 */
@Data
@Accessors(chain = true)
public class UserConfigCreateVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户配置 ID")
    private Long id;

}
