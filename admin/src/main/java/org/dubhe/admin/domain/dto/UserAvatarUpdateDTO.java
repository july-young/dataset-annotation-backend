package org.dubhe.admin.domain.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @description 修改头像

 */
@Data
public class UserAvatarUpdateDTO {

    @Size(max = 255, message = "名称长度不能超过255")
    private String realName;

    @Size(max = 255, message = "头像长度不能超过255")
    private String path;
}
