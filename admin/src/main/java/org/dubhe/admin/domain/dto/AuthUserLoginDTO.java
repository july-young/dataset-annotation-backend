

package org.dubhe.admin.domain.dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description 用户登录dto

 */
@Getter
@Setter
public class AuthUserLoginDTO implements Serializable {

    private static final long serialVersionUID = 6243696246160576285L;
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";

    @Override
    public String toString() {
        return "{username=" + username + ", password= ******}";
    }
}
