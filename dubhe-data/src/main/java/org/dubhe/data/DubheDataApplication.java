

package org.dubhe.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description 数据处理模块服务启动类
 * @date 2020-12-16
 */
@SpringBootApplication(scanBasePackages = "org.dubhe",exclude = ElasticsearchDataAutoConfiguration.class )
@MapperScan(basePackages = {"org.dubhe.**.dao"})
public class DubheDataApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(DubheDataApplication.class, args);
    }

}
