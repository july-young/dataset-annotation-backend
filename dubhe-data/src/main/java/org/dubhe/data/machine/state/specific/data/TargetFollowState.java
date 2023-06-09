
package org.dubhe.data.machine.state.specific.data;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.dao.DatasetMapper;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.machine.enums.DataStateEnum;
import org.dubhe.data.machine.state.AbstractDataState;
import org.dubhe.data.machine.statemachine.DataStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @description 目标跟踪中状态类

 */
@Component
public class TargetFollowState extends AbstractDataState {

    @Autowired
    @Lazy
    private DataStateMachine dataStateMachine;

    @Autowired
    private DatasetMapper datasetMapper;

    /**
     * 目标跟踪完成事件 目标跟踪中-->目标跟踪完成-->目标跟踪完成
     *
     * @param dataset 数据集详情
     */
    @Override
    public void targetCompleteEvent(Dataset dataset){
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.TARGET_COMPLETE_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getTargetFollowState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 目标跟踪失败事件 目标跟踪中-->>目标跟踪失败事件-->>目标跟踪失败
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void autoTrackFailEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(Long.valueOf(primaryKeyId), DataStateEnum.TARGET_FAILURE_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getTargetFailureState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

}