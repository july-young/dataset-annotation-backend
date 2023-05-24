
package org.dubhe.cloud.authconfig.service;

import org.dubhe.biz.base.constant.ApplicationNameConst;
import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.dto.UserDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description admin远程服务调用类

 */
@FeignClient(value = ApplicationNameConst.SERVER_ADMIN,fallback = AdminClientFallback.class)
public interface AdminClient {

    /**
     * 根据用户名称获取用户信息
     *
     * @param username 用户名称
     * @return  用户信息
     */
    @GetMapping(value = "/users/findUserByUsername")
    DataResponseBody<UserContext> findUserByUsername(@RequestParam(value = "username") String username);

    @GetMapping(value = "/users/findById")
    DataResponseBody<UserDTO> getUsers(@RequestParam(value = "userId") Long userId);

    @GetMapping(value = "/users/findByIds")
    DataResponseBody<List<UserDTO>> getUserList(@RequestParam(value = "ids") List<Long> ids);
}
