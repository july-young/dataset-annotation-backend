
package org.dubhe.cloud.remotecall.config;

import org.dubhe.biz.base.constant.AuthConst;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * @description 远程调用默认配置
 * @date 2020-12-11
 */
@Configuration
@EnableFeignClients(basePackages = "org.dubhe")
public class RemoteCallConfig {

    /**
     * 待处理token列表
     */
    public static final List<String> TOKEN_LIST = new LinkedList<>();

    static {
        TOKEN_LIST.add(AuthConst.AUTHORIZATION);
        TOKEN_LIST.add(AuthConst.K8S_CALLBACK_TOKEN);
        TOKEN_LIST.add(AuthConst.COMMON_TOKEN);
    }
}
