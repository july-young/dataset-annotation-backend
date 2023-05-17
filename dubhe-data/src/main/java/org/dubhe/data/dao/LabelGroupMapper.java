

package org.dubhe.data.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dubhe.biz.base.annotation.DataPermission;
import org.dubhe.data.domain.entity.LabelGroup;

/**
 * @description 标签组管理 Mapper 接口
 * @date 2020-09-22
 */
@DataPermission(ignoresMethod = {"insert","getLabelGroupByDataId","selectById"})
public interface LabelGroupMapper extends BaseMapper<LabelGroup> {


    /**
     * 根据数据集ID查询标签组信息
     *
     * @param datasetId 数据集id
     * @return LabelGroup 标签组实体
     */
    @Select("select dlg.id , dlg.label_group_type from data_label_group dlg left join  data_dataset dd on dd.label_group_id = dlg.id\n" +
            "where dd.id = #{datasetId}")
    LabelGroup getLabelGroupByDataId(@Param("datasetId") Long datasetId);


    /**
     * 根据ID修改标签组类型数据
     *
     * @param type          类型
     * @param originUserId  原数据ID
     * @param labelGroupId  标签组ID
     */
    @Update("update data_label_group set type = #{type} , origin_user_id = #{originUserId} where id = #{labelGroupId}")
    void updateInfoByGroupId(@Param("type") Integer type, @Param("originUserId") Long originUserId,
                             @Param("labelGroupId") Long labelGroupId);


    /**
     * 通过标签组ID修改标签状态
     *
     * @param labelGroupId   标签组ID
     * @param deleteFlag     删除标识
     */
    @Update("update data_label_group set deleted = #{deleteFlag} where id = #{labelGroupId}")
    void updateStatusByGroupId(@Param("labelGroupId")Long labelGroupId, @Param("deleteFlag")Boolean deleteFlag);

    /**
     * 删除标签组数据
     *
     * @param groupId 标签组ID
     */
    @Delete("delete from data_label_group where id = #{groupId}")
    void deleteByGroupId(@Param("groupId") Long groupId);
}
