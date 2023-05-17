

package org.dubhe.data.domain.bo;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.constant.TaskSplitStatusEnum;
import org.dubhe.data.domain.entity.File;
import org.dubhe.data.domain.entity.Task;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 任务拆分Bo
 * @date 2020-04-10
 */
@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSplitBO implements Serializable {

    private List<FileBO> files;
    private String id;
    private Integer status;
    private Integer dataType;
    private Integer annotateType;
    private Integer priority;
    private Long taskId;
    private List<String> labels;
    private Integer labelType;
    private Long sendTime;
    private Long datasetId;
    private String versionName;
    private String reTaskId;
    private int algorithm;

    /**
     * 生成任务
     *
     * @param files 当前任务需要处理的文件集合
     * @param task  任务
     * @return TaskSplitBO 生成任务
     */
    public static TaskSplitBO from(List<File> files, Task task) {
        if (CollectionUtils.isEmpty(files)) {
            return null;
        }
        return TaskSplitBO.builder()
                .status(TaskSplitStatusEnum.ING.getValue())
                .files(files.stream().map(FileBO::from).collect(Collectors.toList()))
                .priority(Constant.DEFAULT_PRIORITY)
                .taskId(task.getId())
                .labels(JSON.parseArray(task.getLabels(), String.class))
                .dataType(task.getDataType())
                .annotateType(task.getAnnotateType())
                .build();
    }

}
