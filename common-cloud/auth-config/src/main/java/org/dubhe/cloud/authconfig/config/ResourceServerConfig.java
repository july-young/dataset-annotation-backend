
package org.dubhe.cloud.authconfig.config;

import org.dubhe.biz.base.constant.AuthConst;
import org.dubhe.cloud.authconfig.exception.handler.CustomerAccessDeniedHandler;
import org.dubhe.cloud.authconfig.exception.handler.CustomerTokenExceptionEntryPoint;
import org.dubhe.cloud.authconfig.factory.AccessTokenConverterFactory;
import org.dubhe.cloud.authconfig.factory.TokenServicesFactory;
import org.dubhe.cloud.authconfig.factory.TokenStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

/**
 * @description 资源服务器鉴权配置

 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomerTokenExceptionEntryPoint tokenExceptionEntryPoint;

    @Value("${security.permitAll.matchers:}")
    private String[] permitAllMatchers;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {

        //配置异常处理
        resources.authenticationEntryPoint(tokenExceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        resources
                .tokenStore(tokenStore())
                .stateless(true);
        // 配置RemoteTokenServices，用于向AuthorizationServer验证令牌
        RemoteTokenServices tokenServices = TokenServicesFactory.getTokenServices(accessTokenConverter(),restTemplate);
        resources.tokenServices(tokenServices)
                .stateless(true);


    }

    @Bean
    public JdbcTokenStore tokenStore(){
        return TokenStoreFactory.getJdbcTokenStore(dataSource);
    }

    /**
     * JWT转换器
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        return AccessTokenConverterFactory.getAccessTokenConverter(userDetailsService);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置资源服务器的拦截规则
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // swagger
                .antMatchers(getPermitAllMatchers()).permitAll()
                .anyRequest().authenticated()
        ;
    }

    /**
     * 获取匿名访问路径
     * @return
     */
    private String[] getPermitAllMatchers(){
        String[] c= new String[permitAllMatchers.length + AuthConst.DEFAULT_PERMIT_PATHS.length];
        System.arraycopy(permitAllMatchers, 0, c, 0, permitAllMatchers.length);
        System.arraycopy(AuthConst.DEFAULT_PERMIT_PATHS, 0, c, permitAllMatchers.length, AuthConst.DEFAULT_PERMIT_PATHS.length);
        return c;
    }

}
