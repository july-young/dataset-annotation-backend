package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.base.dto.UserDTO;

import java.util.Set;

@Data
public class AuthUserLoginResultDTO {
    private String token;
    private UserDTO user;
    private Set<String> permissions;
}
