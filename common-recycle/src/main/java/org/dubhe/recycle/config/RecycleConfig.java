package org.dubhe.recycle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description 垃圾回收机制配置常量

 */
@Data
@Component
@ConfigurationProperties(prefix = "recycle.timeout")
public class RecycleConfig {

    /**
     * 回收无效文件的默认有效时长
     */
    private Integer date;

    /**
     * 用户上传文件至临时路径下后文件最大有效时长，以小时为单位
     */
    private Integer fileValid;

}