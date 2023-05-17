package org.dubhe.task.execute.impl;

import com.alibaba.fastjson.JSONObject;
import org.dubhe.data.service.AnnotationService;
import org.dubhe.data.service.TaskService;
import org.dubhe.task.execute.AbstractAlgorithmExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@DependsOn("springContextHolder")
@Component
public class AnnotationQueueExecute extends AbstractAlgorithmExecute {

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    @Lazy
    private TaskService taskService;

    @Override
    public void finishExecute(JSONObject taskDetail) {
        annotationService.finishAnnotation(taskDetail);
    }

    @Override
    public boolean checkStop(Object object, String queueName, JSONObject taskDetail) {
        Long taskId = taskDetail.getLong("taskId");
        return taskService.isStop(taskId);
    }
}