package org.dubhe.recycle.interceptor;

import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.recycle.utils.RecycleTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 资源回收调用拦截器
 * @date 2021-01-21
 */
@Component
public class RecycleCallInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RecycleTool recycleTool;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        String uri = request.getRequestURI();
        LogUtil.debug(LogEnum.GARBAGE_RECYCLE,"资源回收接收到请求,URI:{}",uri);
        String token = request.getHeader(RecycleTool.RECYCLE_TOKEN);
        if (StringUtils.isBlank(token)){
            LogUtil.warn(LogEnum.GARBAGE_RECYCLE,"资源回收没有token配置【{}】,URI:{}",RecycleTool.RECYCLE_TOKEN,uri);
            return false;
        }
        boolean pass = recycleTool.validateToken(token);
        if (!pass){
            LogUtil.warn(LogEnum.GARBAGE_RECYCLE,"资源回收token:【{}】 验证不通过,URI:{}",token,uri);
        }
        return pass;
    }

}
