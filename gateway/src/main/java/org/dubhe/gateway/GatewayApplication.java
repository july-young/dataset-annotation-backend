package org.dubhe.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description gateway启动类
 * @date 2020-12-02
 */
@SpringBootApplication(scanBasePackages = "org.dubhe")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
