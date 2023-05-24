

package org.dubhe.cloud.remotecall.config;




import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.dubhe.biz.base.exception.FeignException;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.context.annotation.Configuration;


/**
 * @description feign 异常处理类

 */

@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        FeignException baseException = null;
        try {
            String errorContent = Util.toString(response.body().asReader());
            DataResponseBody result = JSONObject.parseObject(errorContent, DataResponseBody.class);
            baseException = new FeignException(result.getCode(), result.getMsg());
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR,"FeignClient error :{}",e);
        }
        return baseException;
    }
}
