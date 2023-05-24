
package org.dubhe.cloud.authconfig.service.impl;

import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.service.UserContextService;
import org.dubhe.cloud.authconfig.dto.JwtUserDTO;
import org.dubhe.cloud.authconfig.utils.JwtUtils;
import org.springframework.stereotype.Service;

/**
 * @description OAuth2 当前信息获取实现类

 */
@Service(value = "oAuth2UserContextServiceImpl")
public class OAuth2UserContextServiceImpl implements UserContextService {

    @Override
    public UserContext getCurUser() {
        JwtUserDTO jwtUserDTO = JwtUtils.getCurUser();
        return jwtUserDTO == null ? null : jwtUserDTO.getUser();
    }

    @Override
    public Long getCurUserId() {
        UserContext userContext = getCurUser();
        return userContext == null ? null : userContext.getId();
    }
}
