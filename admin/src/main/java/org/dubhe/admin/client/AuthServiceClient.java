package org.dubhe.admin.client;

import org.dubhe.admin.client.fallback.AuthServiceFallback;
import org.dubhe.biz.base.constant.ApplicationNameConst;
import org.dubhe.biz.base.dto.Oauth2TokenDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = ApplicationNameConst.SERVER_AUTHORIZATION,fallback = AuthServiceFallback.class)
public interface AuthServiceClient {

    /**
     * 获取token
     *
     * @param parameters 获取token请求map
     * @return token 信息
     */
    @PostMapping(value = "/oauth/token")
    DataResponseBody<Oauth2TokenDTO> postAccessToken(@RequestParam Map<String, String> parameters);

    /**
     * 登出
     * @param accessToken token
     * @return
     */
    @DeleteMapping(value="/oauth/logout")
    DataResponseBody<String> logout(@RequestParam("token")String accessToken);

}
