
package org.dubhe.cloud.authconfig.exception.handler;


import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.base.constant.ResponseCode;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @description 权限不足处理类

 */
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException{
        if(!Objects.isNull(e)){
            LogUtil.error(LogEnum.SYS_ERR,"CustomerTokenExceptionEntryPoint accessDeniedException is : {}",e);
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write(JSONObject.toJSONString(DataResponseFactory.failed(ResponseCode.UNAUTHORIZED,"无权访问")));
    }
}