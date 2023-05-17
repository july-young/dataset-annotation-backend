package org.dubhe.admin.task;

import org.dubhe.admin.service.RecycleTaskService;
import org.dubhe.biz.base.constant.UserConstant;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.handler.ScheduleTaskHandler;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.recycle.domain.entity.Recycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description 回收资源定时任务
 * @date 2020-09-21
 */
@Component
public class RecycleResourcesTask {

    @Autowired
    private RecycleTaskService recycleTaskService;

    /**
     * 每天凌晨1点定时回收已删除资源
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void process() {
        ScheduleTaskHandler.process(() -> {
            List<Recycle> recycleTaskList = recycleTaskService.getRecycleTaskList();
            for (Recycle recycle : recycleTaskList) {
                try {
                    recycleTaskService.recycleTask(recycle, UserConstant.ADMIN_USER_ID);
                } catch (Exception e) {
                    LogUtil.error(LogEnum.GARBAGE_RECYCLE, "scheduled recycle task failed，exception {}", e);
                }
            }
        });
    }

}
