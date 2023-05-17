

package org.dubhe.biz.base.exception;


/**
 * @description 异常code
 * @date 2020-11-26
 */
public interface ErrorCode {

    /**
     * 错误码
     * @return code
     */
    Integer getCode();

    /**
     * error info
     * @return
     */
    String getMsg();

}
