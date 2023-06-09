

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @description 用户重置密码请求实体

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户重置密码请求实体", description = "用户重置密码请求实体")
public class UserResetPasswordDTO implements Serializable {


    private static final long serialVersionUID = -4249894291904235207L;

    @NotEmpty(message = "邮箱地址不能为空")
    @Pattern(regexp = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message = "邮箱地址格式有误")
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "xxx@163.com")
    private String email;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码", name = "password", example = "123456")
    private String password;

    @NotNull(message = "激活码不能为空")
    @ApiModelProperty(value = "激活码", name = "code", example = "998877")
    private String code;

}
