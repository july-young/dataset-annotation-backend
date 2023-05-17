
package org.dubhe.data.machine.state.specific.file;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.dao.DatasetVersionFileMapper;
import org.dubhe.data.domain.entity.DatasetVersionFile;
import org.dubhe.data.machine.enums.FileStateEnum;
import org.dubhe.data.machine.state.AbstractFileState;
import org.dubhe.data.machine.statemachine.FileStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 手动标注中状态类
 * @date 2020-09-03
 */
@Component
public class ManualAnnotationFileState extends AbstractFileState {

    @Autowired
    private FileStateMachine fileStateMachine;

    @Autowired
    private DatasetVersionFileMapper datasetVersionFileMapper;

    /**
     * 文件 手动标注中-->点击完成-->标注完成
     *
     * @param datasetVersionFile 数据集版本文件详情
     */
    @Override
    public void saveCompleteEvent(DatasetVersionFile datasetVersionFile) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件前内存中状态机的状态 :{} ", fileStateMachine.getMemoryFileState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", datasetVersionFile);
        LambdaUpdateWrapper<DatasetVersionFile> updatawrapper = new LambdaUpdateWrapper<>();
        if ((datasetVersionFile.getVersionName() == null)) {
            updatawrapper.isNull(DatasetVersionFile::getVersionName);
        } else {
            updatawrapper.eq(DatasetVersionFile::getVersionName, datasetVersionFile.getVersionName());
        }
        updatawrapper.eq(DatasetVersionFile::getDatasetId, datasetVersionFile.getDatasetId());
        updatawrapper.eq(DatasetVersionFile::getFileId, datasetVersionFile.getFileId());
        datasetVersionFileMapper.update(new DatasetVersionFile() {{
                                            setAnnotationStatus(FileStateEnum.ANNOTATION_COMPLETE_FILE_STATE.getCode());
                                            if (datasetVersionFile.getVersionName() != null) {
                                                setChanged(Constant.CHANGED);
                                            }
                                        }},
                updatawrapper);
        fileStateMachine.setMemoryFileState(fileStateMachine.getAnnotationCompleteFileState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【未标注】 执行事件后内存状态机的切换： {}", fileStateMachine.getMemoryFileState());
    }

    /**
     * 文件 手动标注中-->手动标注文件点击保存-->标注中
     *
     * @param datasetVersionFile 版本文件对象
     */
    @Override
    public void manualAnnotationSaveEvent(DatasetVersionFile datasetVersionFile) {
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【手动标注中】 执行事件前内存中状态机的状态 :{} ", fileStateMachine.getMemoryFileState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 接受参数： {} ", datasetVersionFile.toString());
        if (datasetVersionFile.getVersionName() != null) {
            LambdaUpdateWrapper<DatasetVersionFile> updatawrapper = new LambdaUpdateWrapper<>();
            if ((datasetVersionFile.getVersionName() == null)) {
                updatawrapper.isNull(DatasetVersionFile::getVersionName);
            } else {
                updatawrapper.eq(DatasetVersionFile::getVersionName, datasetVersionFile.getVersionName());
            }
            updatawrapper.eq(DatasetVersionFile::getDatasetId, datasetVersionFile.getDatasetId());
            updatawrapper.eq(DatasetVersionFile::getFileId, datasetVersionFile.getFileId());
            datasetVersionFileMapper.update(new DatasetVersionFile() {{
                                                setChanged(Constant.CHANGED);
                                            }},
                    updatawrapper);
        }
        fileStateMachine.setMemoryFileState(fileStateMachine.getManualAnnotationFileState());
        LogUtil.debug(LogEnum.STATE_MACHINE, " 【手动标注中】 执行事件后内存状态机的切换： {}", fileStateMachine.getMemoryFileState());
    }

}