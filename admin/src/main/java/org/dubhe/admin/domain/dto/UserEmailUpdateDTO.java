

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @description 用户邮箱修改信息

 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "邮箱修改请求实体", description = "邮箱修改请求实体")
public class UserEmailUpdateDTO implements Serializable {

    private static final long serialVersionUID = -5997222212073811466L;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", name = "userId", example = "1")
    private Long userId;

    @Pattern(regexp = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message = "邮箱地址格式有误")
    @NotEmpty(message = "邮箱地址不能为空")
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "xx@163.com")
    private String email;


    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码", name = "password", example = "123456")
    private String password;

    @NotNull(message = "激活码不能为空")
    @ApiModelProperty(value = "激活码", name = "code", example = "998877")
    private String code;
}
