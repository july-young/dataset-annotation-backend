
package org.dubhe.admin.task;

import org.dubhe.admin.service.RecycleTaskService;
import org.dubhe.biz.file.config.NfsConfig;
import org.dubhe.biz.log.handler.ScheduleTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @description 回收无效文件资源定时任务

 */
@Component
public class RecycleInvalidResourcesTask {

    @Autowired
    private NfsConfig nfsConfig;

    @Autowired
    private RecycleTaskService recycleTaskService;

    /**
     * 文件存储临时文件根目录
     */
    public static final String UPLOAD_TEMP = File.separator + "upload-temp";

    /**
     * 每天晚上12点定时回收无效文件资源
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void process() {
        ScheduleTaskHandler.process(() -> {
            String sourcePath = nfsConfig.getRootDir() + nfsConfig.getBucket() + UPLOAD_TEMP;
            recycleTaskService.deleteInvalidResourcesByCMD(sourcePath);
        });
    }
}