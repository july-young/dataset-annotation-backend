package org.dubhe.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description 定时任务管理
 */
@SpringBootApplication(scanBasePackages = "org.dubhe")
@MapperScan(basePackages = {"org.dubhe.**.dao"})
@EnableScheduling
public class DubheDataTaskApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(DubheDataTaskApplication.class, args);
    }

}
