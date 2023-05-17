package org.dubhe.task.data;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.dubhe.data.constant.TaskStatusEnum;
import org.dubhe.data.domain.entity.Task;
import org.dubhe.data.service.TaskService;
import org.dubhe.task.constant.TaskQueueNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 王伟
 * @date 2022年06月23日 15:42
 */
@Slf4j
@Component
public class StopTaskSchedule implements Runnable {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void start() {
        Thread thread = new Thread(this, "停止任务处理");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 获取一个需要停止的任务(状态是运行中，且停止状态为true的任务数据)
                Task task = taskService.getOneNeedStopTask();
                if (task != null) {
                    String algorithmName = ObjectUtil.isNull(task.getModelServiceId()) ? TaskQueueNameEnum.getTaskNamespace(task.getType()) : task.getModelServiceId().toString();
                    String key = String.format(TaskQueueNameEnum.STOP_KEY.getTemplate(), algorithmName, task.getDatasetId().toString(), task.getId().toString());
                    redisUtils.del(key);
                    task.setStatus(TaskStatusEnum.FINISHED.getValue());
                    taskService.updateByTaskId(task);
                } else {
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "stop task error");
            }
        }
    }
}
