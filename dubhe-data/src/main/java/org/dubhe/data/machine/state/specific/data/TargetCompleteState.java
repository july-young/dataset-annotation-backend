
package org.dubhe.data.machine.state.specific.data;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.statemachine.exception.StateMachineException;
import org.dubhe.data.dao.DatasetMapper;
import org.dubhe.data.dao.DatasetVersionFileMapper;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.machine.constant.ErrorMessageConstant;
import org.dubhe.data.machine.enums.DataStateEnum;
import org.dubhe.data.machine.state.AbstractDataState;
import org.dubhe.data.machine.statemachine.DataStateMachine;
import org.dubhe.data.machine.utils.StateIdentifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @description 目标跟踪完成状态类

 */
@Component
public class TargetCompleteState extends AbstractDataState {

    @Autowired
    @Lazy
    private DataStateMachine dataStateMachine;

    @Autowired
    private DatasetVersionFileMapper datasetVersionFileMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private StateIdentifyUtil stateIdentify;

    /**
     * 清除标注 标注完成-->未标注
     *
     * @param primaryKeyId 数据集ID
     */
    @Override
    public void deleteAnnotatingEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        Dataset dataset = datasetMapper.selectById(primaryKeyId);
//        datasetVersionFileMapper.update(
//                new DatasetVersionFile() {{
//                    setAnnotationStatus(FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE);
//                    setChanged(Constant.CHANGED);
//                }},
//                new UpdateWrapper<DatasetVersionFile>()
//                        .lambda()
//                        .eq(DatasetVersionFile::getDatasetId, dataset.getId())
//                        .eq(dataset.getCurrentVersionName() != null, DatasetVersionFile::getVersionName, dataset.getCurrentVersionName())
//        );
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.NOT_ANNOTATION_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getNotAnnotationState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());

    }

    /**
     * 目标跟踪完成 自动标注完成-->手动进行标注，点击保存-->标注中
     *
     * @param dataset 数据集详情
     */
    @Override
    public void manualAnnotationSaveEvent(Dataset dataset) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.MANUAL_ANNOTATION_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getAutomaticLabelingState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 目标跟踪完成 未标注/手动标注中/自动标注完成/目标跟踪完成-->点击完成-->标注完成
     *
     * @param dataset 数据集详情
     */
    @Override
    public void finishManualEvent(Dataset dataset){
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        DataStateEnum status = stateIdentify.getStatus(dataset.getId(),dataset.getCurrentVersionName());
        switch (status){
            case ANNOTATION_COMPLETE_STATE:
                //标注完成
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.ANNOTATION_COMPLETE_STATE.getCode(),dataStateMachine.getAnnotationCompleteState());
                break;
            case TARGET_COMPLETE_STATE:
                //目标跟踪完成
                break;
            default:
                throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }


    /**
     * 目标跟踪完成 目标跟踪完成-->算法目标跟踪-->目标跟踪中
     *
     * @param dataset 数据集对象
     */
    @Override
    public void trackEvent(Dataset dataset){
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.TARGET_FOLLOW_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getTargetFollowState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 多视频导入事件  目标跟踪完成 --> 导入视频 --> 采样中
     *
     * @param primaryKeyId 数据集详情
     */
    @Override
    public void sampledEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(Long.valueOf(primaryKeyId), DataStateEnum.SAMPLING_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getSamplingState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 自动标注事件处理方法
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void autoAnnotationsEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【手动标注中】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(Long.valueOf(primaryKeyId), DataStateEnum.AUTOMATIC_LABELING_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getAutomaticLabelingState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【手动标注中】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 删除文件事件
     *
     * @param dataset 数据集详情
     */
    @Override
    public void deleteFilesEvent(Dataset dataset) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        DataStateEnum status = stateIdentify.getStatus(dataset.getId(),dataset.getCurrentVersionName());
        // 删除后数据集的状态可能变为(目标跟踪完成，标注完成, 未标注)
        AbstractDataState memoryDataState = null;
        switch (status){
            case TARGET_COMPLETE_STATE:
                memoryDataState = dataStateMachine.getTargetCompleteState();
                break;
            case ANNOTATION_COMPLETE_STATE:
                memoryDataState = dataStateMachine.getAnnotationCompleteState();
                break;
            case NOT_ANNOTATION_STATE:
                memoryDataState = dataStateMachine.getNotAnnotationState();
                break;
            default:
                throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        dataStateMachine.doStateChange(dataset.getId(), status.getCode(), memoryDataState);
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【目标跟踪完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

}