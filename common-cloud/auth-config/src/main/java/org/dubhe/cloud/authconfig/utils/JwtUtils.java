
package org.dubhe.cloud.authconfig.utils;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.cloud.authconfig.dto.JwtUserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @description JWT

 */
public class JwtUtils {

    private JwtUtils(){

    }

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    public static JwtUserDTO getCurUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null){
                return null;
            }
            if (authentication.getPrincipal() instanceof JwtUserDTO){
                return (JwtUserDTO) authentication.getPrincipal();
            }
        }catch (Exception e){
            LogUtil.error(LogEnum.SYS_ERR,"Jwt getCurUser error!{}",e);
        }
        return null;
    }

    /**
     * 获取当前用户ID
     * 若用户不存在，则返回null（可根据业务统一修改）
     * @return 当前用户ID
     */
    public static Long getCurUserId(){
        JwtUserDTO jwtUserDTO = getCurUser();
        return jwtUserDTO == null?null:jwtUserDTO.getCurUserId();
    }
}
