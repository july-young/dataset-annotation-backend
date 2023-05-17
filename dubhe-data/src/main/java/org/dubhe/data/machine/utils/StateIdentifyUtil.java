package org.dubhe.data.machine.utils;

import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.machine.enums.DataStateEnum;
import org.dubhe.data.machine.utils.identify.data.DataHub;
import org.dubhe.data.machine.utils.identify.setting.StateIdentifySetting;
import org.dubhe.data.machine.utils.identify.setting.StateSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @description 状态判断实现类
 * @date 2020-09-24
 */
@Component
public class StateIdentifyUtil {


    /**
     * 数据查询处理
     */
    @Autowired
    private DataHub dataHub;

    /**
     * 获取数据集状态(指定版本)
     *
     * @param datasetId               数据集id
     * @param versionName             数据集版本名称
     * @return DatasetStatusEnum      数据集状态(指定版本)
     */
    public DataStateEnum getStatus(Long datasetId, String versionName) {
        IdentifyDatasetStateByFileState fileState = new IdentifyDatasetStateByFileState(datasetId, versionName, StateIdentifySetting.NEED_FILE_STATE_DO_IDENTIFY);
        return fileState.getStatus();
    }

    public DataStateEnum getStatus(Long datasetId) {
        return dataHub.getDatasetStatus(datasetId);
    }

    /**
     * 获取数据集状态(未指定版本)
     *
     * @param dataset                 数据集
     * @param needFileStateDoIdentify 是否需要查询文件状态判断
     * @return DatasetStatusEnum      数据集状态(指定版本)
     */
    public DataStateEnum getStatus(Dataset dataset, boolean needFileStateDoIdentify) {
        return needFileStateDoIdentify ? new IdentifyDatasetStateByFileState(dataset.getId(), dataset.getCurrentVersionName(), StateIdentifySetting.NEED_FILE_STATE_DO_IDENTIFY)
                .getStatus() : dataHub.getDatasetStatus(dataset.getId());
    }

    /**
     * 获取数据集状态(自动标注/目标跟踪回滚使用)
     *
     * @param datasetId   数据集id
     * @param versionName 数据集版本名称
     * @return DatasetStatusEnum    数据集状态(指定版本)
     */
    public DataStateEnum getStatusForRollback(Long datasetId, String versionName) {
        return new IdentifyDatasetStateByFileState(datasetId, versionName, StateIdentifySetting.ROLL_BACK_FOR_STATE).getStatus();
    }


    class IdentifyDatasetStateByFileState {

        /**
         * 判断得到的数据集状态
         */
        public DataStateEnum state;

        /**
         * 会查询文件的状态去对数据集的状态做判断
         *
         * @param datasetId   数据集ID
         * @param versionName 数据集版本名称
         */
        public IdentifyDatasetStateByFileState(Long datasetId, String versionName, Set<DataStateEnum> dataStateEnums) {
            state = dataHub.getDatasetStatus(datasetId);
            if (dataStateEnums.contains(state)) {
                List<Integer> stateList = dataHub.getFileStatusListByDatasetAndVersion(datasetId, versionName);
                DataStateEnum res = StateSelect.judge(stateList);
                if (res != null) {
                    state = res;
                    return;
                }
            }
        }

        DataStateEnum getStatus() {
            return this.state;
        }
    }
}