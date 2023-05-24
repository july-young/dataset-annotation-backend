package org.dubhe.task.data;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.handler.ScheduleTaskHandler;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.service.impl.DatasetVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description 已发布版本文件复制定时任务

 */
@Component
public class FileCopySchedule {

    @Autowired
    private DatasetVersionServiceImpl datasetVersionService;

    /**
     * 文件复制
     */
    @Scheduled(cron = "*/15 * * * * ?")
    public void fileCopy() {
        ScheduleTaskHandler.process(() -> {
            LogUtil.info(LogEnum.BIZ_DATASET, "file copy --- > start");
            datasetVersionService.fileCopy();
            LogUtil.info(LogEnum.BIZ_DATASET, "file copy --- > end");
        });
    }

}
