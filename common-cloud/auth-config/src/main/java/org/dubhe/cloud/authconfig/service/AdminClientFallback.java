
package org.dubhe.cloud.authconfig.service;

import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.dto.UserDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description admin远程服务调用类熔断

 */
@Component
public class AdminClientFallback implements AdminClient {
    @Override
    public DataResponseBody<UserContext> findUserByUsername(String username) {
        return DataResponseFactory.failed("call admin server findUserByUsername error");
    }

    @Override
    public DataResponseBody<UserDTO> getUsers(Long userId) {
        return DataResponseFactory.failed("call user controller to get user error");
    }

    @Override
    public DataResponseBody<List<UserDTO>> getUserList(List<Long> ids) {
        return DataResponseFactory.failed("call user controller to get users error");
    }

}