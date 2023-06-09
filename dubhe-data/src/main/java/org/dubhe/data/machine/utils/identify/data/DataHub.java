package org.dubhe.data.machine.utils.identify.data;

import org.dubhe.data.machine.enums.DataStateEnum;
import org.dubhe.data.service.DatasetService;
import org.dubhe.data.service.DatasetVersionFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description 数据查询/加工

 */
@Component
public class DataHub {

    /**
     * 数据集版本文件关系服务类
     */
    @Autowired
    private DatasetVersionFileService datasetVersionFileService;

    /**
     * 数据集服务类
     */
    @Autowired
    @Lazy
    private DatasetService datasetService;

    /**
     * 获取数据集的状态
     * @param datasetId 数据集ID
     * @return          数据集状态枚举
     */
    public  DataStateEnum getDatasetStatus(Long datasetId){
        Integer status = datasetService.getOneById(datasetId).getStatus();
        return DataStateEnum.getState(status);
    }

    /**
     * 获取数据集下文件的状态（数据经过去重处理）
     * @param datasetId     数据集ID
     * @param versionName   数据集版本名称
     * @return              数据集下文件状态的并集
     */
    public  List<Integer> getFileStatusListByDatasetAndVersion(Long datasetId, String versionName) {
        return datasetVersionFileService.getFileStatusListByDatasetAndVersion(datasetId, versionName);
    }

}
