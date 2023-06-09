

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dubhe.admin.domain.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description 用户中心修改DTO

 */
@Data
public class UserCenterUpdateDTO implements Serializable {

    private static final long serialVersionUID = -6196691710092809498L;
    @NotNull(groups = User.Update.class)
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 255, message = "昵称长度不能超过255")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private String sex;

    @NotBlank
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$", message = "手机号格式有误")
    private String phone;

    private String remark;

}
