
package org.dubhe.data.statemachine.dto;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * @description  执行状态机切换请求体

 */
@Component
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StateChangeDTO {

    /**
     * 业务参数 eg： id
     */
    private Object[] objectParam;

    /**
     * 状态机类型 eg：dataStateMachine
     */
    private String stateMachineType;

    /**
     * 状态机执行事件
     */
    private String eventMethodName;

}