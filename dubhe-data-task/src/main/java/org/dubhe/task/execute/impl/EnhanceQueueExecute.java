package org.dubhe.task.execute.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.domain.bo.EnhanceTaskSplitBO;
import org.dubhe.data.domain.dto.DatasetEnhanceFinishDTO;
import org.dubhe.data.service.TaskService;
import org.dubhe.data.service.impl.DatasetEnhanceServiceImpl;
import org.dubhe.task.execute.AbstractAlgorithmExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@DependsOn("springContextHolder")
@Component
public class EnhanceQueueExecute extends AbstractAlgorithmExecute {

    @Autowired
    private DatasetEnhanceServiceImpl datasetEnhanceService;

    @Autowired
    @Lazy
    private TaskService taskService;

    @Override
    public void finishExecute(JSONObject taskDetail) {
        DatasetEnhanceFinishDTO datasetEnhanceFinishDTO = JSONObject.parseObject(taskDetail.get("object").toString()
                , DatasetEnhanceFinishDTO.class);
        LogUtil.info(LogEnum.BIZ_DATASET, "start finish enhance task datasetEnhanceFinishDTO:{}", datasetEnhanceFinishDTO);
        datasetEnhanceService.enhanceFinish(datasetEnhanceFinishDTO,taskDetail);
    }

    @Override
    public void failExecute(JSONObject failDetail) {
        EnhanceTaskSplitBO enhanceTaskSplitBO = JSON.parseObject(failDetail.toJSONString(), EnhanceTaskSplitBO.class);
        Integer fileNum = enhanceTaskSplitBO.getFileDtos().size();
        taskService.finishTaskFile(enhanceTaskSplitBO, fileNum);
    }

    @Override
    public boolean checkStop(Object object, String queueName, JSONObject taskDetail) {
        Long taskId = taskDetail.getLong("id");
        return taskService.isStop(taskId);
    }
}
