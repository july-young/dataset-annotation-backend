package org.dubhe.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class DataRedisConfig {

    private String database;

    private String host;

    private String password;

    private String port;
}
