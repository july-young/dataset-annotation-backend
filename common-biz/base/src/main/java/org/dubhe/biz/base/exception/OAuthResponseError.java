
package org.dubhe.biz.base.exception;

import org.dubhe.biz.base.vo.DataResponseBody;
import org.springframework.http.HttpStatus;

/**
 * @description OAuth2 授权自定义异常

 */
public class OAuthResponseError extends RuntimeException{

    private DataResponseBody responseBody;

    private HttpStatus statusCode;

    public OAuthResponseError(HttpStatus statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.responseBody = new DataResponseBody(statusCode.value(),msg,null);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public DataResponseBody getResponseBody() {
        return responseBody;
    }
}
