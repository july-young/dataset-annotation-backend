
package org.dubhe.data.machine.state.specific.data;

import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.statemachine.exception.StateMachineException;
import org.dubhe.data.constant.DatatypeEnum;
import org.dubhe.data.constant.ErrorEnum;
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
 * @description 自动标注完成类
 * @date 2020-08-27
 */
@Component
public class AutoTagCompleteState extends AbstractDataState {

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
     * 自动标注完成   自动标注完成-->调用增强算法-->增强中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void strengthenEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(primaryKeyId, DataStateEnum.STRENGTHENING_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getStrengtheningState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 自动标注完成   自动标注完成-->上传图片-->标注中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void uploadPicturesEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(primaryKeyId, DataStateEnum.MANUAL_ANNOTATION_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getManualAnnotationState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 自动标注完成事件 自动标注完成-->未标注
     *
     * @param primaryKeyId 数据集Id
     */
    @Override
    public void deletePictureNotMarkedEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(primaryKeyId, DataStateEnum.NOT_ANNOTATION_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getNotAnnotationState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 自动标注完成 标注完成-->清除标注-->未标注
     *
     * @param primaryKeyId 数据集ID
     */
    @Override
    public void deleteAnnotatingEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        Dataset dataset = datasetMapper.selectById(primaryKeyId);
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.NOT_ANNOTATION_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getNotAnnotationState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 自动标注完成 自动标注完成-->手动进行标注，点击保存-->标注中
     *
     * @param dataset 数据集详情
     */
    @Override
    public void manualAnnotationSaveEvent(Dataset dataset) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.MANUAL_ANNOTATION_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getAutomaticLabelingState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 标注完成事件 未标注/手动标注中/自动标注完成/目标跟踪完成-->点击完成-->标注完成
     *
     * @param dataset 数据集详情
     */
    @Override
    public void finishManualEvent(Dataset dataset){
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        DataStateEnum status = stateIdentify.getStatus(dataset.getId(),dataset.getCurrentVersionName());
        switch (status){
            case AUTO_TAG_COMPLETE_STATE:
                break;
            case ANNOTATION_COMPLETE_STATE:
                //标注完成
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.ANNOTATION_COMPLETE_STATE.getCode(),dataStateMachine.getAnnotationCompleteState());
                break;
            case NOT_ANNOTATION_STATE:
                //未标注
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.NOT_ANNOTATION_STATE.getCode(),dataStateMachine.getNotAnnotationState());
                break;
            case MANUAL_ANNOTATION_STATE:
                //手动标注中
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.MANUAL_ANNOTATION_STATE.getCode(),dataStateMachine.getManualAnnotationState());
                break;
            default:
                throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 目标跟踪中事件 自动标注完成-->算法目标跟踪-->目标跟踪中
     *
     * @param dataset 数据集对象
     */
    @Override
    public void trackEvent(Dataset dataset){
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        datasetMapper.updateStatus(dataset.getId(), DataStateEnum.TARGET_FOLLOW_STATE.getCode());
        dataStateMachine.setMemoryDataState(dataStateMachine.getTargetFollowState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 删除文件事件
     *
     * @param dataset 数据集详情
     */
    @Override
    public void deleteFilesEvent(Dataset dataset) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        DataStateEnum status = stateIdentify.getStatus(dataset.getId(),dataset.getCurrentVersionName());
        switch (status){
            case AUTO_TAG_COMPLETE_STATE:
                //自动标注完成
                break;
            case ANNOTATION_COMPLETE_STATE:
                //标注完成
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.ANNOTATION_COMPLETE_STATE.getCode(),dataStateMachine.getAnnotationCompleteState());
                break;
            case NOT_ANNOTATION_STATE:
                //未标注
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.NOT_ANNOTATION_STATE.getCode(),dataStateMachine.getNotAnnotationState());
                break;
            default:
                throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 上传文件事件
     *
     * @param dataset 数据集详情
     */
    @Override
    public void uploadFilesEvent(Dataset dataset) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", dataset);
        DataStateEnum status = stateIdentify.getStatus(dataset.getId(),dataset.getCurrentVersionName());
        switch (status){
            case MANUAL_ANNOTATION_STATE:
                //手动标注中
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.MANUAL_ANNOTATION_STATE.getCode(),dataStateMachine.getManualAnnotationState());
                break;
            case ANNOTATION_COMPLETE_STATE:
                //标注完成
                dataStateMachine.doStateChange(dataset.getId(),DataStateEnum.ANNOTATION_COMPLETE_STATE.getCode(),dataStateMachine.getManualAnnotationState());
                break;
            default:
                throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【自动标注完成】 执行事件后内存状态机的切换： {}", dataStateMachine.getMemoryDataState());
    }

    /**
     * 多视频导入事件  自动标注完成 --> 导入视频 --> 采样中
     *
     * @param primaryKeyId 数据集详情
     */
    @Override
    public void sampledEvent(Long primaryKeyId) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件前内存中状态机的状态 :{} ", dataStateMachine.getMemoryDataState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", primaryKeyId);
        datasetMapper.updateStatus(primaryKeyId, DataStateEnum.SAMPLING_STATE.getCode());
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

}
