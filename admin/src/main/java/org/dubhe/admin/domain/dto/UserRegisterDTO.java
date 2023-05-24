

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
 * @description 用户注册请求实体

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册请求实体", description = "用户注册请求实体")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = -7351676930575145394L;

    @ApiModelProperty(value = "账号", name = "username", example = "test")
    @NotEmpty(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "昵称", name = "nickName", example = "xt")
    private String nickName;

    @ApiModelProperty(value = "性别：1、男，0、女", name = "sex", example = "1")
    private Integer sex;

    @NotEmpty(message = "邮箱地址不能为空")
    @Pattern(regexp = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message = "邮箱地址格式有误")
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "xxx@163.com")
    private String email;

    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$", message = "手机号格式有误")
    @ApiModelProperty(value = "手机号", name = "phone", example = "13823370116")
    private String phone;


    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码", name = "password", example = "123456")
    private String password;

    @NotNull(message = "激活码不能为空")
    @ApiModelProperty(value = "激活码", name = "code", example = "998877")
    private String code;

}
