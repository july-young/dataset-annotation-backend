package org.dubhe.data.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class EsProperties {

    /**
     * es服务地址
     */
    @Value("${es.host}")
    private String host;

    /**
     * es同步端口
     */
    @Value("${es.transportPort}")
    private Integer transportPort;

    /**
     * es同步端口
     */
    @Value("${es.serverPort}")
    private Integer serverPort;

    /**
     * 集群名称
     */
    @Value("${es.clusterName}")
    private String clusterName;


}
