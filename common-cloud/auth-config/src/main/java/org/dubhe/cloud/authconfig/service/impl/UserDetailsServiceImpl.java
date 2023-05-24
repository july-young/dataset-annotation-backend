
package org.dubhe.cloud.authconfig.service.impl;

import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.dto.SysPermissionDTO;
import org.dubhe.biz.base.dto.SysRoleDTO;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.cloud.authconfig.dto.JwtUserDTO;
import org.dubhe.cloud.authconfig.service.AdminUserService;
import org.dubhe.cloud.authconfig.service.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description 自定义用户实现类（未实现对应数据库持久化设置）

 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${spring.application.name}")
    private String curService;

    @Autowired(required = false)
    private AdminUserService adminUserService;

    @Resource
    private AdminClient adminClient;

    /**
     * 捞取用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserContext userContext;
        //根据adminUserService 子类是否加载 采取不同的方式获取用户信息
        if (adminUserService == null) {
            DataResponseBody<UserContext> responseBody = adminClient.findUserByUsername(username);
            if (responseBody == null || responseBody.getData() == null || !responseBody.succeed()) {
                throw new BusinessException(responseBody.getMsg());
            }
            userContext = responseBody.getData();
        } else {
            userContext = adminUserService.findUserByUsername(username);
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('user')") 标注的接口对比，决定是否可以调用接口
        Set<String> permissions = findPermissions(userContext.getRoles());
        List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        return new JwtUserDTO(userContext, grantedAuthorities);
    }

    /**
     * 固定权限
     *
     * @return
     */
    private Set<String> findPermissions(List<SysRoleDTO> roles) {
        Set<String> permissions = new HashSet<>();
        roles.forEach(a -> {
            permissions.add("ROLE_" + a.getName());
            List<SysPermissionDTO> permissionsNames = a.getPermissions();
            if (!CollectionUtils.isEmpty(permissionsNames)) {
                permissionsNames.forEach(b -> {
                    permissions.add("ROLE_" + b.getPermission());
                });

            }
        });
        return permissions;
    }

}
