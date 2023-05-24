
package org.dubhe.data.statemachine.exception;

import lombok.Getter;
import org.dubhe.biz.base.exception.BusinessException;

/**
 * @description 状态机异常类

 */
@Getter
public class StateMachineException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 自定义状态机异常(抛出异常堆栈信息)
     *
     * @param cause
     */
    public StateMachineException(Throwable cause){
        super(cause);
    }

    /**
     * 自定义状态机异常(抛出异常信息)
     *
     * @param msg
     */
    public StateMachineException(String msg){
        super(msg);
    }

}