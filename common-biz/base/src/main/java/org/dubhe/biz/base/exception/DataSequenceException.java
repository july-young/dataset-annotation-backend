

package org.dubhe.biz.base.exception;

import lombok.Getter;
import org.dubhe.biz.base.exception.BusinessException;

/**
 * @description 获取序列异常

 */
@Getter
public class DataSequenceException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public DataSequenceException(String msg) {
        super(msg);
    }

    public DataSequenceException(String msg, Throwable cause) {
        super(msg,cause);
    }

    public DataSequenceException(Throwable cause) {
        super(cause);
    }
}
