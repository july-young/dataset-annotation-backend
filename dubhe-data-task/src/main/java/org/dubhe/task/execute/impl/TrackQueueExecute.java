

package org.dubhe.task.execute.impl;

import com.alibaba.fastjson.JSONObject;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.data.machine.statemachine.DataStateMachine;
import org.dubhe.data.statemachine.dto.StateChangeDTO;
import org.dubhe.data.domain.dto.AutoTrackCreateDTO;
import org.dubhe.data.domain.entity.Task;
import org.dubhe.data.machine.constant.DataStateMachineConstant;
import org.dubhe.data.service.AnnotationService;
import org.dubhe.data.service.TaskService;
import org.dubhe.task.execute.AbstractAlgorithmExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@DependsOn("springContextHolder")
@Component
public class TrackQueueExecute extends AbstractAlgorithmExecute {

    @Autowired
    @Lazy
    private TaskService taskService;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private DataStateMachine dataStateMachine;

    @Override
    public void finishExecute(JSONObject taskDetail) {
        Long id = taskDetail.getLong("id");
        Task task = taskService.detail(id);
        Boolean flag = taskService.finishTask(id, 1);
        if (flag) {
            AutoTrackCreateDTO autoTrackCreateDTO = new AutoTrackCreateDTO();
            autoTrackCreateDTO.setCode(MagicNumConstant.TWO_HUNDRED);
            autoTrackCreateDTO.setData(null);
            autoTrackCreateDTO.setMsg("success");
            annotationService.finishAutoTrack(task.getDatasetId(), autoTrackCreateDTO);
        }
    }

    @Override
    public void failExecute(JSONObject failDetail) {
        Long id = failDetail.getLong("id");
        Task task = taskService.detail(id);
        Boolean flag = taskService.finishTask(id, MagicNumConstant.ONE);
        if (flag) {
            //嵌入状态机（目标跟踪中—>目标跟踪失败）
            dataStateMachine.autoTrackFailEvent(task.getDatasetId());
        }
    }

    @Override
    public boolean checkStop(Object object, String queueName, JSONObject taskDetail) {
        Long taskId = taskDetail.getLong("id");
        return taskService.isStop(taskId);
    }
}
