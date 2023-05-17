package org.dubhe.task.data;

import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.handler.ScheduleTaskHandler;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.data.service.DatasetVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description 标注文件复制
 */
@Component
public class AnnotationCopySchedule {

    @Autowired
    private DatasetVersionService datasetVersionService;

    /**
     * 标注文件复制
     */
    @Scheduled(fixedDelay = 15000)
    public void annotationFileCopy() {
        ScheduleTaskHandler.process(() -> {
            LogUtil.info(LogEnum.BIZ_DATASET, "annotation file copy and roll back --- > start");
            datasetVersionService.annotationFileCopy();
            LogUtil.info(LogEnum.BIZ_DATASET, "annotation file copy and roll back --- > end");
        });
    }

}
