

package org.dubhe.biz.base.exception;

import lombok.Getter;
import org.dubhe.biz.base.constant.ResponseCode;
import org.dubhe.biz.base.enums.BaseErrorCodeEnum;
import org.dubhe.biz.base.vo.DataResponseBody;

/**
 * @description 业务异常

 */
@Getter
public class BusinessException extends RuntimeException {

    private String msg;
    private Integer code;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
        this.code = ResponseCode.ERROR;
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
        this.code = ResponseCode.ERROR;
        this.msg = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.code = ResponseCode.ERROR;
        this.msg = super.getMessage();
    }

    public BusinessException(Integer code, String msg, String info, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = info == null ? msg : msg + ":" + info;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        this(errorCode.getCode(), errorCode.getMsg(), null, cause);
    }

    public BusinessException(ErrorCode errorCode, String info, Throwable cause) {
        this(errorCode.getCode(), errorCode.getMsg(), info, cause);
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
