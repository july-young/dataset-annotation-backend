package org.dubhe.data.machine.state.specific.data;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.dao.DatasetMapper;
import org.dubhe.data.machine.enums.DataStateEnum;
import org.dubhe.data.machine.state.AbstractDataState;
import org.dubhe.data.machine.statemachine.DataStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @description 未采样状态类
 * @date 2020-08-27
 */
@Component
public class NotSampledState extends AbstractDataState {

    @Autowired
    @Lazy
    private DataStateMachine dataStateMachine;

    @Autowired
    private DatasetMapper datasetMapper;

    /**
     * 数据集 未采样-->调用采集图片程序-->采样中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void sampledEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未采样】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(primaryKeyId, DataStateEnum.SAMPLING_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getSamplingState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未采样】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }


}