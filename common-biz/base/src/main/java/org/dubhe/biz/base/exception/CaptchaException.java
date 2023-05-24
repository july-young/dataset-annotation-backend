

package org.dubhe.biz.base.exception;

import lombok.Getter;
import org.dubhe.biz.base.constant.ResponseCode;
import org.dubhe.biz.base.vo.DataResponseBody;

/**
 * @description  验证码异常

 */
@Getter
public class CaptchaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private DataResponseBody responseBody;
    private Throwable cause;

    public CaptchaException(String msg) {
        this.responseBody = new DataResponseBody(ResponseCode.BADREQUEST, msg);
    }

    public CaptchaException(String msg, Throwable cause) {
        this.cause = cause;
        this.responseBody = new DataResponseBody(ResponseCode.BADREQUEST, msg);
    }

    public CaptchaException(Throwable cause) {
        this.cause = cause;
        this.responseBody = new DataResponseBody(ResponseCode.BADREQUEST);
    }
}
