
package org.dubhe.cloud.authconfig.exception.handler;


import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.base.constant.ResponseCode;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * @description token无效处理类
 * @date 2020-12-21
 */
@Component
public class CustomerTokenExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        if(!Objects.isNull(e)){
            LogUtil.error(LogEnum.SYS_ERR,"CustomerTokenExceptionEntryPoint authenticationException is : {}",e);
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write(JSONObject.toJSONString(DataResponseFactory.failed(ResponseCode.TOKEN_ERROR,"token无效或已过期")));
    }
}
