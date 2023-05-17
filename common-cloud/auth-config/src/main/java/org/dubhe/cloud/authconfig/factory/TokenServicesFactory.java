
package org.dubhe.cloud.authconfig.factory;

import org.dubhe.biz.base.constant.AuthConst;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @description TokenServices 工厂类
 * @date 2020-11-25
 */
public class TokenServicesFactory {

    private TokenServicesFactory(){

    }

    /**
     * 获取 RemoteTokenServices
     * @param accessTokenConverter token转换器
     * @param restTemplate  rest请求模板
     * @return RemoteTokenServices
     */
    public static RemoteTokenServices getTokenServices(JwtAccessTokenConverter accessTokenConverter, RestTemplate restTemplate){
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        if (accessTokenConverter != null){
            tokenServices.setAccessTokenConverter(accessTokenConverter);
        }
        // 配置异常处理器
//        restTemplate.setErrorHandler(new OAuth2ResponseErrorHandler());
        tokenServices.setRestTemplate(restTemplate);
        tokenServices.setCheckTokenEndpointUrl(AuthConst.CHECK_TOKEN_ENDPOINT_URL);
        tokenServices.setClientId(AuthConst.CLIENT_ID);
        tokenServices.setClientSecret(AuthConst.CLIENT_SECRET);
        return tokenServices;
    }

}
