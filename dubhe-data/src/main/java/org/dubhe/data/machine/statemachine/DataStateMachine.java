package org.dubhe.data.machine.statemachine;

import lombok.Data;
import org.dubhe.biz.base.utils.SpringContextHolder;
import org.dubhe.data.statemachine.exception.StateMachineException;
import org.dubhe.data.dao.DatasetMapper;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.machine.constant.ErrorMessageConstant;
import org.dubhe.data.machine.enums.DataStateEnum;
import org.dubhe.data.machine.state.AbstractDataState;
import org.dubhe.data.machine.state.specific.data.*;
import org.dubhe.data.machine.utils.StateIdentifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @description 数据状态机

 */
@Data
@Component
@Scope(value = "request",proxyMode= ScopedProxyMode.TARGET_CLASS)
public class DataStateMachine extends AbstractDataState implements Serializable {

    /**
     * 标注完成
     */
    @Autowired
    private AnnotationCompleteState annotationCompleteState;

    /**
     * 自动标注中
     */
    @Autowired
    private AutomaticLabelingState automaticLabelingState;

    /**
     * 自动标注完成
     */
    @Autowired
    private AutoTagCompleteState autoTagCompleteState;

    /**
     * 手动标注中
     */
    @Autowired
    private ManualAnnotationState manualAnnotationState;

    /**
     * 未标注
     */
    @Autowired
    private NotAnnotationState notAnnotationState;

    /**
     * 未采样
     */
    @Autowired
    private NotSampledState notSampledState;

    /**
     * 采样中
     */
    @Autowired
    private SamplingState samplingState;

    /**
     * 采样失败
     */
    @Autowired
    private SampledFailureState sampledFailureState;

    /**
     * 增强中
     */
    @Autowired
    private StrengtheningState strengtheningState;

    /**
     * 目标跟踪完成
     */
    @Autowired
    private TargetCompleteState targetCompleteState;

    /**
     * 目标跟踪失败
     */
    @Autowired
    private TargetFailureState targetFailureState;

    /**
     * 目标跟踪中
     */
    @Autowired
    private TargetFollowState targetFollowState;

    /**
     * 导入中
     */
    @Autowired
    private IsTheImportState importState;


    @Autowired
    private DatasetMapper datasetMapper;

    /**
     * 内存中的状态机
     */
    private AbstractDataState memoryDataState;


    @Autowired
    private StateIdentifyUtil stateIdentify;

    /**
     * 改变数据集的状态
     *
     * @param datasetId        数据集ID
     * @param status           数据集状态
     * @param dataStateMachine 内存中的状态
     */
    public void doStateChange(Long datasetId, Integer status, AbstractDataState dataStateMachine) {
        datasetMapper.updateStatus(datasetId, status);
        this.memoryDataState = dataStateMachine;
    }

    /**
     * 初始化状态机的状态
     *
     * @param primaryKeyId 业务ID
     * @return Dataset 数据集详情
     */
    public Dataset initMemoryDataState(Long primaryKeyId) {
        if (primaryKeyId == null) {
            throw new StateMachineException("未找到业务ID");
        }
        Dataset dataset = datasetMapper.selectById(primaryKeyId);
        if (dataset == null || dataset.getStatus() == null) {
            throw new StateMachineException("未找到业务数据");
        }
        memoryDataState = SpringContextHolder.getBean(DataStateEnum.getStateMachine(dataset.getStatus()));
        return dataset;
    }

    /**
     * 清除标注 目标跟踪完成-->手动修改标注，对修改的标注点击保存-->手动标注中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void deleteAnnotatingEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (
                memoryDataState != autoTagCompleteState &&
                        memoryDataState != annotationCompleteState &&
                        memoryDataState != targetCompleteState &&
                        memoryDataState != manualAnnotationState
        ) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }

        memoryDataState.deleteAnnotatingEvent(primaryKeyId);
    }


    /**
     * 目标跟踪事件 目标跟踪失败/自动标注完成/标注完成/目标跟踪完成-->目标跟踪-->目标跟踪中
     *
     * @param dataset 数据集对象
     */
    @Override
    public void trackEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState != targetFailureState &&
                memoryDataState != autoTagCompleteState &&
                memoryDataState != annotationCompleteState &&
                memoryDataState != targetCompleteState
        ) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        memoryDataState.trackEvent(dataset);
    }

    /**
     * 目标跟踪失败事件 目标跟踪中-->>目标跟踪失败事件-->>目标跟踪失败
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void autoTrackFailEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != targetFollowState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        targetFollowState.autoTrackFailEvent(primaryKeyId);
    }

    /**
     * 目标跟踪完成事件 目标跟踪中-->目标跟踪完成-->目标跟踪完成
     *
     * @param dataset 数据集详情
     */
    @Override
    public void targetCompleteEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState != targetFollowState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        targetFollowState.targetCompleteEvent(dataset);
    }

    /**
     * 自动标注事件  手动标注中/未标注-->点击自动标注-->自动标注中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void autoAnnotationsEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState == automaticLabelingState || memoryDataState == notSampledState
                || memoryDataState == samplingState || memoryDataState == strengtheningState
                || memoryDataState == targetFollowState || memoryDataState == importState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        memoryDataState.autoAnnotationsEvent(primaryKeyId);
    }

    /**
     * 自动标注中事件 自动标注中-->自动标注完成-->自动标注完成
     *
     * @param dataset 数据集详情
     */
    @Override
    public void doFinishAutoAnnotationEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState != automaticLabelingState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        automaticLabelingState.doFinishAutoAnnotationEvent(dataset);
    }

    /**
     * 采样事件   未采样-->调用采集图片程序-->采样中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void sampledEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState == samplingState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        samplingState.sampledEvent(primaryKeyId);
    }

    /**
     * 采样事件   采样中-->调用采集图片程序-->采样失败
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void samplingFailureEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != samplingState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        samplingState.samplingFailureEvent(primaryKeyId);
    }

    /**
     * 采样事件   采样中-->调用采集图片程序-->未标注
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void samplingEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != samplingState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        samplingState.samplingEvent(primaryKeyId);
    }

    /**
     * 自动标注事件   未标注-->自动标准算法-->自动标注中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void autoAnnotationEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != notAnnotationState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        notAnnotationState.autoAnnotationEvent(primaryKeyId);
    }

    /**
     * 手动标注事件  手动标注中-->手动标注完成-->手动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void manualAnnotationCompleteEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != manualAnnotationState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        manualAnnotationState.manualAnnotationCompleteEvent(primaryKeyId);
    }


    /**
     * 手动标注事件  手动标注中-->删除文件,文件只包含自动标注完成-->自动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void manualAutomaticLabelingCompletionEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != manualAnnotationState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        manualAnnotationState.manualAutomaticLabelingCompletionEvent(primaryKeyId);
    }

    /**
     * 手动标注事件  手动标注中-->文件删除-->未标注
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void manualNotMarkedEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != manualAnnotationState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        manualAnnotationState.manualNotMarkedEvent(primaryKeyId);
    }

    /**
     * 自动标注事件  自动标注中-->调用自动标注算法-->自动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void automaticLabelingEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != automaticLabelingState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        automaticLabelingState.automaticLabelingEvent(primaryKeyId);
    }

    /**
     * 手动标注保存 自动标注完成-->手动进行标注，点击保存-->标注中
     *
     * @param dataset 数据集详情
     */
    @Override
    public void manualAnnotationSaveEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState == manualAnnotationState) {
            return;
        }
        if (memoryDataState != notAnnotationState
                && memoryDataState != autoTagCompleteState
                && memoryDataState != annotationCompleteState
                && memoryDataState != targetCompleteState
        ) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        memoryDataState.manualAnnotationSaveEvent(dataset);

    }

    /**
     * 标注完成事件 未标注/手动标注中/自动标注完成/目标跟踪完成-->点击完成-->标注完成
     *
     * @param dataset 数据集详情
     */
    @Override
    public void finishManualEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (
                memoryDataState != notAnnotationState &&
                        memoryDataState != manualAnnotationState &&
                        memoryDataState != autoTagCompleteState &&
                        memoryDataState != targetCompleteState &&
                        memoryDataState != annotationCompleteState
        ) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        memoryDataState.finishManualEvent(dataset);
    }

    /**
     * 自动标注完成   自动标注完成-->调用增强算法-->增强中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void strengthenEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != autoTagCompleteState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        autoTagCompleteState.strengthenEvent(primaryKeyId);
    }


    /**
     * 自动标注完成   自动标注完成-->新上传图片，然后点击自定标注按钮-->自动标注中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void uploadPicturesEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != autoTagCompleteState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        autoTagCompleteState.uploadPicturesEvent(primaryKeyId);
    }

    /**
     * 标注完成事件  标注完成-->上传图片-->标注中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void uploadSavePicturesEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != annotationCompleteState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        annotationCompleteState.uploadSavePicturesEvent(primaryKeyId);
    }

    /**
     * 标注完成事件  标注完成-->调用增强算法-->增强中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void completeStrengthenEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != annotationCompleteState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        annotationCompleteState.completeStrengthenEvent(primaryKeyId);
    }

    /**
     * 标注完成事件  标注完成-->删除图片-->未标注
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void deletePicturesEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != annotationCompleteState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        annotationCompleteState.deletePicturesEvent(primaryKeyId);
    }

    /**
     * 增强中状态事件  增强中-->标注完成-->标注完成
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void strengtheningCompleteEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != strengtheningState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        strengtheningState.strengtheningCompleteEvent(primaryKeyId);
    }

    /**
     * 增强中状态事件  增强中-->自动标注完成-->自动标注完成
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void strengtheningAutoCompleteEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != strengtheningState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        strengtheningState.strengtheningAutoCompleteEvent(primaryKeyId);
    }

    /**
     * 自动标注完成事件 自动标注完成-->未标注
     *
     * @param primaryKeyId 数据集Id
     */
    @Override
    public void deletePictureNotMarkedEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState != autoTagCompleteState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        autoTagCompleteState.deletePictureNotMarkedEvent(primaryKeyId);
    }

    /**
     * 删除文件事件
     *
     * @param dataset 数据集详情
     */
    @Override
    public void deleteFilesEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState == notAnnotationState) {
            return;
        }
        if (memoryDataState != manualAnnotationState &&
                memoryDataState != autoTagCompleteState &&
                memoryDataState != annotationCompleteState &&
                memoryDataState != targetCompleteState
        ) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        memoryDataState.deleteFilesEvent(dataset);
    }

    /**
     * 上传文件事件
     *
     * @param dataset 数据集详情
     */
    @Override
    public void uploadFilesEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState == manualAnnotationState) {
            return;
        }
        if (memoryDataState != autoTagCompleteState
                && memoryDataState != annotationCompleteState
                && memoryDataState != notAnnotationState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        memoryDataState.uploadFilesEvent(dataset);
    }

    /**
     * 增强完成事件
     *
     * @param dataset 数据集详情
     */
    @Override
    public void enhanceFinishEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState != strengtheningState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        strengtheningState.enhanceFinishEvent(dataset);
    }

    /**
     * 表格导入事件 未标注 --> 导入表格 --> 导入中
     *
     * @param primaryKeyId 业务ID
     */
    @Override
    public void tableImportEvent(Long primaryKeyId) {
        initMemoryDataState(primaryKeyId);
        if (memoryDataState == importState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        importState.tableImportEvent(primaryKeyId);
    }

    /**
     * 表格导入完成时间 导入中 --> 解析表格 --> 未标注
     *
     * @param dataset 数据集详情
     */
    @Override
    public void tableImportFinishEvent(Dataset dataset) {
        initMemoryDataState(dataset.getId());
        if (memoryDataState != importState) {
            throw new StateMachineException(ErrorMessageConstant.DATASET_CHANGE_ERR_MESSAGE);
        }
        importState.tableImportFinishEvent(dataset);
    }

}
