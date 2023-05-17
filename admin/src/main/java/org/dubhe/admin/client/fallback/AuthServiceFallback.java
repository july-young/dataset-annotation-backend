package org.dubhe.admin.client.fallback;

import org.dubhe.admin.client.AuthServiceClient;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;

import java.util.Map;

public class AuthServiceFallback implements AuthServiceClient {


    /**
     * 获取token
     *
     * @param parameters 获取token请求map
     * @return token 信息
     */
    @Override
    public DataResponseBody postAccessToken(Map<String, String> parameters) {
        return DataResponseFactory.failed("call auth server postAccessToken error ");
    }

    /**
     * 退出登录
     *
     * @param accessToken token
     * @return
     */
    @Override
    public DataResponseBody logout(String accessToken) {
        return DataResponseFactory.failed("call auth server logout error ");
    }
}
