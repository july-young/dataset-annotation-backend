package org.dubhe.data.config;

import org.apache.http.HttpHost;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description Es Rest 客户端配置
 * @date 2021-03-24
 */
@Configuration
public class EsRestClientConfiguration {

    @Autowired
    private EsProperties esProperties;

    /**
     * Es Rest 客户端配置
     *
     * @return
     */
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient getClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esProperties.getHost(), esProperties.getServerPort(), "http")));
    }


    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        return ReactiveRestClients.create(ClientConfiguration.create(esProperties.getHost() + ":" + esProperties.getServerPort()));
    }

    /**
     * 初始化ES批量处理操作类
     *
     * @return
     * @throws UnknownHostException 未知主机异常
     */
    @Bean(name = "bulkProcessor")
    public BulkProcessor bulkProcessor() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", esProperties.getClusterName()).build();
        Client client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(esProperties.getHost()), esProperties.getTransportPort()));
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            }

        };

        return BulkProcessor.builder(client, listener).setBulkActions(MagicNumConstant.ONE_THOUSAND)
                .setBulkSize(new ByteSizeValue(MagicNumConstant.FIVE, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(MagicNumConstant.FIVE))
                .setConcurrentRequests(MagicNumConstant.ONE)
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(MagicNumConstant.ONE_HUNDRED), MagicNumConstant.THREE))
                .build();
    }
}
