package org.dubhe.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description Auth启动类
 * @date 2020-12-02
 */
@SpringBootApplication(scanBasePackages = "org.dubhe")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
