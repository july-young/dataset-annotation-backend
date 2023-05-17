
package org.dubhe.cloud.authconfig.factory;

import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @description TokenStore工厂类
 * @date 2020-11-24
 */
public class TokenStoreFactory {

    private TokenStoreFactory(){

    }

    /**
     * 获取token存储
     * @param dataSource   数据库数据源
     * @return
     */
    public static JdbcTokenStore getJdbcTokenStore(DataSource dataSource){
        return new JdbcTokenStore(dataSource);
    }
}
