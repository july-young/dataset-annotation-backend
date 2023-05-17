
package org.dubhe.cloud.authconfig.dto;

import org.dubhe.biz.base.context.UserContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @description 安全用户模型
 * @date 2020-10-29
 */
public class JwtUserDTO <U extends UserContext> implements UserDetails{

    private static final long serialVersionUID = 1L;

    private U user;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDTO(U user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public Long getCurUserId(){
        return user.getId();
    }

    /**
     * 获取当前用户信息（不建议全部暴露，建议根据业务需要暴露可暴露信息）
     * @return
     */
    public U getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
