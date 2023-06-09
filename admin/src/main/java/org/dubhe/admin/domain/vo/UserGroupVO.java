
package org.dubhe.admin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 用户组信息
 * @date 2021-05-11
 */
@Data
public class UserGroupVO implements Serializable {

    private static final long serialVersionUID = -1584916470133872539L;

    @ApiModelProperty("用户组Id")
    private Long id;

    @ApiModelProperty("用户组名称")
    private String name;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("修改人")
    private Long updateUserId;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    @ApiModelProperty("修改时间")
    private Timestamp updateTime;

    @ApiModelProperty("描述")
    private String description;
}
