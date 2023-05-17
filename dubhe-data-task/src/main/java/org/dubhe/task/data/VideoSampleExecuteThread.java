package org.dubhe.task.data;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.dubhe.data.constant.DataTaskTypeEnum;
import org.dubhe.data.constant.TaskStatusEnum;
import org.dubhe.data.domain.entity.Task;
import org.dubhe.data.service.TaskService;
import org.dubhe.task.constant.DataAlgorithmEnum;
import org.dubhe.task.constant.TaskQueueNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @description 视频抽帧任务分发线程
 * @date 2022-04-06
 */
@Slf4j
@Component
public class VideoSampleExecuteThread implements Runnable {

    /**
     * 路径名前缀
     */
    @Value("${storage.file-store-root-path:/nfs/}")
    private String prefixPath;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 启动生成任务线程
     */
    @PostConstruct
    public void start() {
        Thread thread = new Thread(this, "抽帧任务生成");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                work();
                TimeUnit.MILLISECONDS.sleep(MagicNumConstant.ONE_THOUSAND);
            } catch (Exception e) {
                LogUtil.error(LogEnum.BIZ_DATASET, "get frame_split task failed:{}", e);
            }
        }
    }

    /**
     * 单个任务处理
     */
    public void work() {
        // 获取一个待抽帧任务
        QueryWrapper<Task> pendingQuery = new QueryWrapper<>();
        pendingQuery.lambda().eq(Task::getStatus, TaskStatusEnum.UN_ASSIGN.getValue())
                .eq(Task::getType, DataTaskTypeEnum.VIDEO_SAMPLE.getValue())
                .eq(Task::isStop, false);
        List<Task> pendingTasks = taskService.selectByQueryWrapper(pendingQuery);
        Integer[] statuses = new Integer[]{TaskStatusEnum.ASSIGNING.getValue(), TaskStatusEnum.RUNNING.getValue()};
        QueryWrapper<Task> proceedQuery = new QueryWrapper<>();
        proceedQuery.lambda().eq(Task::getType, DataTaskTypeEnum.VIDEO_SAMPLE.getValue())
                .in(Task::getStatus, statuses);
        List<Task> proceedTasks = taskService.selectByQueryWrapper(proceedQuery);
        List<Long> proceedDatasetIds = proceedTasks.stream().map(Task::getDatasetId).collect(Collectors.toList());
        //只能处理单个数据集的抽帧任务
        List<Task> filterTasks = pendingTasks.stream()
                .filter(task -> !proceedDatasetIds.contains(task.getDatasetId())).sorted(Comparator.comparing(Task::getDatasetId))
                .collect(Collectors.toList());
        for (Task filterTask : filterTasks) {
            int count = taskService.updateTaskStatus(filterTask.getId(), TaskStatusEnum.UN_ASSIGN.getValue(), TaskStatusEnum.ASSIGNING.getValue());
            if (count != 0) {
                execute(filterTask);
                taskService.updateTaskStatus(filterTask.getId(), TaskStatusEnum.ASSIGNING.getValue(), TaskStatusEnum.RUNNING.getValue());
            }
        }
    }

    /**
     * 执行抽帧任务
     *
     * @param task 任务详情
     */
    public void execute(Task task) {
        videoSampleExecute(task);
    }

    /**
     * 采样任务
     *
     * @param task 任务详情
     */
    private void videoSampleExecute(Task task) {
        java.io.File file = new java.io.File(prefixPath + task.getUrl());
        int lengthInFrames = 0;
        try {
            FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(file);
            ff.start();
            lengthInFrames = ff.getLengthInVideoFrames();
            ff.stop();
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_DATASET, "get frames error:{}", e);
        }
        List<Integer> frames = new ArrayList<>();
        for (int i = 1; i < lengthInFrames; ) {
            frames.add(i);
            i += task.getFrameInterval();
        }
        List<List<Integer>> framesSplitTasks = CollectionUtil.split(frames, 500);
        taskService.setTaskTotal(task.getId(), framesSplitTasks.size());
        AtomicInteger j = new AtomicInteger(1);
        framesSplitTasks.forEach(framesSplitTask -> {
            JSONObject param = new JSONObject();
            param.put("datasetId", task.getDatasetId() + ":" + j);
            param.put("path", prefixPath + task.getUrl());
            param.put("frames", framesSplitTask);
            param.put("id", task.getId().toString());
            param.put("algorithm", DataAlgorithmEnum.VIDEO_SAMPLE.getAlgorithmType());
            JSONObject paramKey = new JSONObject();
            paramKey.put("datasetIdKey", task.getDatasetId() + ":" + j);
            String detailKey = UUID.randomUUID().toString();
            String taskQueue = TaskQueueNameEnum.getTemplate(
                    TaskQueueNameEnum.TASK,
                    TaskQueueNameEnum.TaskQueueConfigEnum.VIDEO_SAMPLE,
                    String.valueOf(task.getDatasetId()),
                    task.getId().toString()
            );

            String detail = TaskQueueNameEnum.getTemplate(
                    TaskQueueNameEnum.DETAIL,
                    TaskQueueNameEnum.TaskQueueConfigEnum.VIDEO_SAMPLE,
                    String.valueOf(task.getDatasetId()),
                    task.getId().toString(),
                    detailKey
            );

            redisUtils.zSet(taskQueue, -1, detailKey);
            redisUtils.set(detail, param);

            j.addAndGet(1);
        });
    }
}
