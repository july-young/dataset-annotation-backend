
package org.dubhe.cloud.authconfig.service;


import org.dubhe.biz.base.context.UserContext;

public interface AdminUserService {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    UserContext findUserByUsername(String username);
}
