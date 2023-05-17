
package org.dubhe.data.machine.proxy;

import org.dubhe.biz.base.utils.SpringContextHolder;
import org.dubhe.data.statemachine.dto.StateChangeDTO;
import org.dubhe.data.statemachine.utils.StateMachineProxyUtil;
import org.dubhe.data.machine.statemachine.GlobalStateMachine;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description 代理执行状态机
 * @date 2020-08-27
 */
@Component
public class StateMachineProxy {

    /**
     * 获取全局状态机Bean
     *
     * @return Object
     */
    public static Object getGlobalStateMachine() {
        return SpringContextHolder.getBean(GlobalStateMachine.class);
    }

    /**
     * 代理执行单个状态机的状态切换
     *
     * @param stateChangeDTO 数据集状态切换信息
     */
    public static void proxyExecutionSingleState(StateChangeDTO stateChangeDTO) {
        StateMachineProxyUtil.proxyExecutionSingleState(stateChangeDTO,getGlobalStateMachine());
    }

    /**
     * 代理执行多个状态机的状态切换
     *
     * @param stateChangeDTOList 多个状态机切换信息
     */
    public static void proxyExecutionRelationState(List<StateChangeDTO> stateChangeDTOList) {
        StateMachineProxyUtil.proxyExecutionRelationState(stateChangeDTOList,getGlobalStateMachine());
    }

}
