

package org.dubhe.data.service;

import com.alibaba.fastjson.JSONObject;
import org.dubhe.data.domain.dto.DatasetEnhanceFinishDTO;
import org.dubhe.data.domain.dto.DatasetEnhanceRequestDTO;
import org.dubhe.data.domain.entity.DatasetVersionFile;
import org.dubhe.data.domain.entity.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description 数据集增强服务

 */
public interface DatasetEnhanceService {

    /**
     * 提交任务
     *
     * @param datasetVersionFiles      数据集版本文件列表
     * @param task                     任务
     * @param datasetEnhanceRequestDTO 提交任务参数
     */
    void commitEnhanceTask(List<DatasetVersionFile> datasetVersionFiles, Task task, DatasetEnhanceRequestDTO datasetEnhanceRequestDTO,String taskQueue,String detail,String placeholder);

    /**
     * 增强任务完成
     *
     * @param datasetEnhanceFinishDTO 数据集增强完成详情
     */
    void enhanceFinish(DatasetEnhanceFinishDTO datasetEnhanceFinishDTO, JSONObject taskDetail);

}
