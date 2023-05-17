
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dubhe.biz.db.base.PageQueryBase;

import java.io.Serializable;

/**
 * @description 分页查看权限组列表
 * @date 2021-05-14
 */

@Data
public class AuthCodeQueryDTO extends PageQueryBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限组名称")
    private String authCode;

}
