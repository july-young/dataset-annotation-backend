
package org.dubhe.cloud.unittest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @description 配置dev环境单元测试用户名与密码
 * @date 2021-01-11
 */
@Data
@Component
@ConfigurationProperties(prefix = "unittest")
@RefreshScope
public class UnitTestConfig {

    private String username;

    private String password;

}