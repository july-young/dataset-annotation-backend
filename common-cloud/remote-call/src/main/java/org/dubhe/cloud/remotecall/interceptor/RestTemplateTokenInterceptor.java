
package org.dubhe.cloud.remotecall.interceptor;

import org.dubhe.biz.base.constant.AuthConst;
import org.dubhe.cloud.remotecall.config.RemoteCallConfig;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @description RestTemplate token拦截处理器
 * @date 2020-11-26
 */
public class RestTemplateTokenInterceptor implements ClientHttpRequestInterceptor {

    /**
     * 对 RestTemplate 的token进行拦截传递
     * @param httpRequest the request, containing method, URI, and headers
     * @param body the body of the request
     * @param execution the request execution
     * @return the response
     * @throws IOException in case of I/O errors
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RemoteCallConfig.TOKEN_LIST.forEach(token -> {
            if (AuthConst.AUTHORIZATION.equals(token)){
                do4Auth2Token(httpRequest);
            }else {
                do4DefinedToken(httpRequest,token);
            }
        });
        return execution.execute(httpRequest, body);
    }

    /**
     * 处理自定义token传递
     * @param httpRequest the request, containing method, URI, and headers
     * @param token     token名称
     */
    private void do4DefinedToken(HttpRequest httpRequest,String token) {
        List<String> authorizations = httpRequest.getHeaders().get(token);
        if (CollectionUtils.isEmpty(authorizations)){
            return;
        }
        httpRequest.getHeaders().add(token, authorizations.get(0));
    }

    /**
     * 处理Auth2授权token传递
     * @param httpRequest the request, containing method, URI, and headers
     */
    private void do4Auth2Token(HttpRequest httpRequest) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String authToken = request.getHeader(AuthConst.AUTHORIZATION);
        String accessToken = request.getParameter(AuthConst.ACCESS_TOKEN);
        List<String> authorizations = httpRequest.getHeaders().get(AuthConst.AUTHORIZATION);
        String authorization = null;
        if (accessToken != null){
            authorization = AuthConst.ACCESS_TOKEN_PREFIX + accessToken;
        }else if (authToken != null){
            authorization = authToken;
        }else if (authorizations != null){
            authorization = AuthConst.ACCESS_TOKEN_PREFIX + authorizations.get(0);
        }
        httpRequest.getHeaders().add(AuthConst.AUTHORIZATION, authorization);
    }

}
