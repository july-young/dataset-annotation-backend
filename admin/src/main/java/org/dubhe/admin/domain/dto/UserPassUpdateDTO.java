

package org.dubhe.admin.domain.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @description 修改密码的
 * @date 2020-06-01
 */
@Data
public class UserPassUpdateDTO {

    @Size(max = 255, message = "密码长度不能超过255")
    private String oldPass;

    @Size(max = 255, message = "密码长度不能超过255")
    private String newPass;
}
