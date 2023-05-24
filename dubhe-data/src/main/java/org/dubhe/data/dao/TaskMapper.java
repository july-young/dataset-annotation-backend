

package org.dubhe.data.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dubhe.data.domain.entity.Task;

/**
 * @description 标注任务信息 Mapper 接口

 */
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 任务完成数加一
     *
     * @param id            文件ID
     * @param filesCount    文件数量
     * @return int   执行次数
     */
    @Update("update data_task t set t.finished = t.finished + #{filesCount} where t.id = #{id}")
    int finishFile(@Param("id") Long id, @Param("filesCount") Integer filesCount);

    /**
     * 修改完成任务数量
     *
     * @param id      文件ID
     * @param fileNum 文件数量
     * @return int    执行次数
     */
    @Update("update data_task t set t.finished = t.finished + #{fileNum} WHERE t.id = #{id}")
    int finishFileNum(@Param("id") Long id, @Param("fileNum") Integer fileNum);

    @Update("update data_task task set task.status=3 where task.id=#{taskId}")
    void taskStop(@Param("taskId")Long taskId);

    @Select("select max(task.id) from data_task task where task.status in (0,1,2) and task.dataset_id=#{datasetId} and (select dataset.status from data_dataset dataset where dataset.id=#{datasetId})=#{datasetStatus}")
    Long selectTaskId(@Param("datasetId") Long datasetId,@Param("datasetStatus") Integer datasetStatus);

    @Select("select max(task.id) from data_task task where task.status = 3 and task.dataset_id=#{datasetId} and (select dataset.status from data_dataset dataset where dataset.id=#{datasetId})=#{datasetStatus} and task.type != 12 and task.id < #{taskId}")
    Long selectStopTaskId(@Param("taskId") Long taskId,@Param("datasetId") Long datasetId,@Param("datasetStatus") Integer datasetStatus);

}
