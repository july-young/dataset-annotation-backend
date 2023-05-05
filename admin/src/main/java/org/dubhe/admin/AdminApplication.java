package org.dubhe.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @description Admin启动类
 * @date 2020-12-02
 */
@SpringBootApplication(scanBasePackages = "org.dubhe")
@MapperScan(basePackages = {"org.dubhe.**.dao"})
@EnableScheduling
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
