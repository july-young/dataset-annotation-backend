
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.base.PageQueryBase;

import java.io.Serializable;

/**
 * @description 获取用户组信息
 * @date 2021-05-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class UserGroupQueryDTO extends PageQueryBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户组id或名称")
    private String keyword;

}
