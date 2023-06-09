package org.dubhe.task.constant;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 *└── dataset
 *     └── {算法名称}
 *         ├── {dataset_id}
 *         │ ├── annotation
 *         │ │ └── {taskId}
 *         │ │     ├── B.json
 *         │ │     └── C.json
 *         │ ├── detail
 *         │ │ └── {taskId}
 *         │ │     ├── B.json
 *         │ │     ├── C.json
 *         │ │     ├── E.json
 *         │ │     ├── F.json
 *         │ │     └── H.json
 *         │ ├── failed
 *         │ │ └── {taskId}
 *         │ │     └── task_E
 *         │ ├── finished
 *         │ │ └── {taskId}
 *         │ │     ├── task_B
 *         │ │     └── task_C
 *         │ ├── processing
 *         │ │ └── {taskId}
 *         │ │     └── task_F
 *         │ └── task
 *         │     └── {taskId}
 *         │         └── task_H
 *         ├── failed_task
 *         │ └── task_D
 *         ├── finished_task
 *         │ └── task_A
 *         └── pre_task
 *             └── task_G
 *
 */
@Getter
public enum TaskQueueNameEnum {


    DETAIL("dataset:%s:%s:%s:detail:%s","任务详情"),

    FINISHED("dataset:%s:%s:%s:finished:%s","完成队列"),

    PROCESSING("dataset:%s:%s:%s:processing:%s","进行中队列"),

    ANNOTATION("dataset:%s:%s:%s:annotation:%s","标注内容"),

    TASK("dataset:%s:%s:%s:task:001","待处理队列"),

    FAILED("dataset:%s:%s:%s:failed:%s","处理失败队列"),

    FAILED_TASK("dataset:%s","flag"),

    FINISHED_TASK("dataset:%s","flag"),

    STOP_KEY("dataset:%s:%s:%s:*", "停止时队列前缀"),

    PRE_TASK("dataset:%s","flag");

    TaskQueueNameEnum(String template, String desc) {
        this.template = template;
        this.desc = desc;
    }

    /**
     * redis 存储队列
     */
    private String template;

    /**
     * 说明
     */
    private String desc;

    @Getter
    public enum TaskQueueConfigEnum {

        ANNOTATION("annotation", "普通标算法"),

        IMAGE_NET("image_net", "图片分类算法"),

        IMG_PROCESS("img_process", "图片增强"),

        LUNG_SEGMENTATION("lung_segmentation", "肺部分割"),

        OF_RECORD("of_record", "ofrecord转换"),

        TEXT_CLASSIFICATION("text_classification", "文本分类"),

        TRACK("track", "目标跟踪"),

        VIDEO_SAMPLE("video_sample", "视频抽帧算法"),

        ALL("*","所有算法");

        TaskQueueConfigEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        private String name;
        private String desc;
    }

    public static String getTemplate(TaskQueueNameEnum queueNameEnum,TaskQueueConfigEnum configEnum,String ...args) {
        if (ObjectUtils.isEmpty(args)){
            return String.format(queueNameEnum.template, configEnum.name);
        }
        Object[] array = new ArrayList<String>() {{
            add(configEnum.name);
            addAll(Arrays.asList(args));
        }}.toArray();
        return String.format(queueNameEnum.template,array);
    }

    public static String getTaskNamespace(Integer type) {
        switch (type) {
            case 1:
                return TaskQueueConfigEnum.OF_RECORD.name;
            case 3:
                return TaskQueueConfigEnum.IMG_PROCESS.name;
            case 5:
                return TaskQueueConfigEnum.VIDEO_SAMPLE.name;
            default:
                return null;
        }
    }

}
