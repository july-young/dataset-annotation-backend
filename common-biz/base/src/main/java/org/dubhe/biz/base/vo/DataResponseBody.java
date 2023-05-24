

package org.dubhe.biz.base.vo;


import lombok.Data;
import org.dubhe.biz.base.constant.ResponseCode;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * @description 统一的公共响应体

 */
@Data
public class DataResponseBody<T> implements Serializable {

    /**
     * 返回状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 泛型数据
     */
    private T data;
    /**
     * 链路追踪ID
     */
    private String traceId;

    public DataResponseBody() {
        this(ResponseCode.SUCCESS, null);
    }

    public DataResponseBody(T data) {
        this(ResponseCode.SUCCESS, null, data);
    }

    public DataResponseBody(Integer code, String msg) {
        this(code, msg, null);
    }

    public DataResponseBody(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.traceId = MDC.get("traceId");
    }

    /**
     * 判断是否响应成功
     * @return ture 成功，false 失败
     */
    public boolean succeed(){
        return ResponseCode.SUCCESS.equals(this.code);
    }

}
