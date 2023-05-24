

package org.dubhe.data.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dubhe.data.domain.entity.DatasetGroupLabel;

import java.util.List;
import java.util.Map;

/**
 * @description 标签组标签中间表 Mapper 接口

 */
public interface DatasetGroupLabelMapper extends BaseMapper<DatasetGroupLabel> {


    /**
     * 根据标签组ID删除标签和标签组的关联关系
     *
     * @param groupId 标签组ID
     */
    @Delete("delete from data_group_label where label_group_id = #{groupId} ")
    void deleteByGroupId(@Param("groupId") Long groupId);

    /**
     * 通过标签组ID修改标签状态
     *
     * @param labelGroupId   标签组ID
     * @param deleteFlag     删除标识
     */
    @Update("update data_group_label set deleted = #{deleteFlag} where label_group_id = #{labelGroupId}")
    void updateStatusByGroupId(@Param("labelGroupId")Long labelGroupId, @Param("deleteFlag")Boolean deleteFlag);

    /**
     * 更具标签组ID获取标签Ids
     *
     * @param groupId 标签组ID
     * @return 标签Ids
     */
    @Select("select label_id from data_group_label where label_group_id = #{groupId}")
    List<Long> getLabelIdsByGroupId(@Param("groupId") Long groupId);


    /**
     * 根据标签组ID查询标签数据量
     *
     * @param groupIds 标签组列表
     * @return  标签列表
     */
    List<DatasetGroupLabel> getLabelByGroupIds(List<Long> groupIds);

    /**
     * 标签组标签数据统计
     *
     * @param groupIds 标签组列表
     * @return
     */
    List<Map<Long, Long>> getLabelGroupCount(List<Long> groupIds);

}
