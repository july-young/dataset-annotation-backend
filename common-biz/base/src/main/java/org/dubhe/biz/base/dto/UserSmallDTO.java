

package org.dubhe.biz.base.dto;

import lombok.Data;

/**
 * @description 用户信息DTO
 * @date 2020-06-01
 */
@Data
public class UserSmallDTO {
    private String username;
    private String nickName;

    public UserSmallDTO() {}

    public UserSmallDTO(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.nickName = userDTO.getNickName();
    }

}
