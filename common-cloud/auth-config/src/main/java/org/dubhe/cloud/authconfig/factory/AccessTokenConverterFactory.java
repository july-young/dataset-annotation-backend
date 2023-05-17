
package org.dubhe.cloud.authconfig.factory;

import org.dubhe.biz.base.constant.AuthConst;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @description AccessTokenConverter 工厂类
 * @date 2020-11-24
 */
public class AccessTokenConverterFactory {

    private AccessTokenConverterFactory(){}


    /**
     * 获取JwtAccessTokenConverter
     * @param userDetailsService  自定义实现的用户信息获取Service
     * @return jwtAccessTokenConverter
     */
    public static JwtAccessTokenConverter getAccessTokenConverter(UserDetailsService userDetailsService){
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailsService);

        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        converter.setUserTokenConverter(userAuthenticationConverter);

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(AuthConst.CLIENT_SECRET);
        jwtAccessTokenConverter.setAccessTokenConverter(converter);
        return jwtAccessTokenConverter;
    }
}
