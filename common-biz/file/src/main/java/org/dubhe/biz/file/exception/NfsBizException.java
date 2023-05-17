

package org.dubhe.biz.file.exception;

import lombok.Getter;
import org.dubhe.biz.base.exception.BusinessException;

/**
 * @description NFS utils 工具异常
 * @date 2020-06-15
 */
@Getter
public class NfsBizException extends BusinessException {

    private static final long serialVersionUID = 1L;


    public NfsBizException(Throwable cause){
        super(cause);
    }

    public NfsBizException(String msg){
        super(msg);
    }


}
