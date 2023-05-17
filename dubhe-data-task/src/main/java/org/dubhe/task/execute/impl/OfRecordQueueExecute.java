package org.dubhe.task.execute.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dubhe.data.domain.dto.OfRecordTaskDto;
import org.dubhe.data.service.DatasetVersionService;
import org.dubhe.data.service.TaskService;
import org.dubhe.task.execute.AbstractAlgorithmExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@DependsOn("springContextHolder")
@Component
public class OfRecordQueueExecute extends AbstractAlgorithmExecute {

    @Autowired
    private TaskService taskService;

    @Autowired
    private DatasetVersionService datasetVersionService;

    @Override
    public void finishExecute(JSONObject taskDetail) {
        OfRecordTaskDto ofRecordTaskDto = JSON.parseObject(JSON.toJSONString(taskDetail), OfRecordTaskDto.class);
        Boolean flag = taskService.finishTask(ofRecordTaskDto.getId(), ofRecordTaskDto.getFiles().size());
        if (flag) {
            datasetVersionService.update(ofRecordTaskDto.getDatasetVersionId(), 5, 2);
        }
    }

    @Override
    public boolean checkStop(Object object, String queueName, JSONObject taskDetail) {
        Long taskId = taskDetail.getLong("id");
        return taskService.isStop(taskId);
    }
}
