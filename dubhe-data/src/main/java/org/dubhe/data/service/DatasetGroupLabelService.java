

package org.dubhe.data.service;

import org.dubhe.data.domain.entity.DatasetGroupLabel;

import java.util.List;
import java.util.Map;

/**
 * @description 标签组标签中间表服务

 */
public interface DatasetGroupLabelService {


    /**
     * 新增标签组标签中间表信息
     *
     * @param datasetGroupLabel 新增标签组标签中间表实体
     * @return  新增新增标签组标签中间表结果
     */
    int insert(DatasetGroupLabel datasetGroupLabel);

    /**
     * 根据标签组id查询标签关联关系列表
     *
     * @param groupId 标签组id
     * @return  标签标签组关联关系列表
     */
    List<DatasetGroupLabel> listByGroupId(Long groupId);

    /**
     * 根据标签id查询标签关联关系列表
     *
     * @param labelId 标签id
     * @return  标签标签组关联关系列表
     */
    List<DatasetGroupLabel> listByLabelId(Long labelId);

    /**
     * 根据标签组ID删除标签和标签组的关联关系
     *
     * @param groupId 标签组ID
     */
    void deleteByGroupId(Long groupId);

    /**
     * 通过标签组ID修改标签状态
     *
     * @param labelGroupId   标签组ID
     * @param deleteFlag     删除标识
     */
    void updateStatusByGroupId(Long labelGroupId, Boolean deleteFlag);

    /**
     * 更具标签组ID获取标签Ids
     *
     * @param groupId 标签组ID
     * @return 标签Ids
     */
    List<Long> getLabelIdsByGroupId(Long groupId);

    /**
     * 根据标签组ID查询标签数据量
     *
     * @param groupIds 标签组列表
     * @return  key:标签组ID value:标签数量
     */
    Map<Long, Integer> getLabelByGroupIds(List<Long> groupIds);
}
