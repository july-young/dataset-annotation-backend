

package org.dubhe.data.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dubhe.biz.base.annotation.DataPermission;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.domain.vo.DatasetVersionQueryVO;

import java.util.List;

/**
 * @description 数据集管理 Mapper 接口
 * @date 2020-04-10
 */
@DataPermission(ignoresMethod = {"insert", "selectById", "selectCountByPublic", "selectList", "dataVersionListVO"})
public interface DatasetMapper extends BaseMapper<Dataset> {

    /**
     * 分页获取数据集
     *
     * @param page          分页插件
     * @param queryWrapper  查询条件
     * @return Page<Dataset>数据集列表
     */
    @Select("SELECT * FROM data_dataset ${ew.customSqlSegment}")
    Page<Dataset> listPage(Page<Dataset> page, @Param("ew") Wrapper<Dataset> queryWrapper);

    /**
     * 修改数据集当前版本
     *
     * @param id          数据集ID
     * @param versionName 数据集版本名称
     */
    @Update("update data_dataset set current_version_name = #{versionName}  where id = #{id}")
    void updateVersionName(@Param("id") Long id, @Param("versionName") String versionName);

    /**
     * 更新数据集状态
     *
     * @param datasetId 数据集ID
     * @param status    数据集状态
     */
    @Update("update data_dataset set status = #{status} where id = #{datasetId}")
    void updateStatus(@Param("datasetId") Long datasetId, @Param("status") Integer status);

    /**
     * 根据标签组ID查询关联的数据集数量
     *
     * @param labelGroupId 标签组ID
     * @return int 数量
     */
    @Select("SELECT count(1) FROM data_dataset where label_group_id = #{labelGroupId}")
    int getCountByLabelGroupId(@Param("labelGroupId") Long labelGroupId);


    /**
     * 数据集数据删除
     *
     * @param id            数据集id
     * @param deleteFlag    删除标识
     * @return int 数量
     */
    @Update("update data_dataset set deleted = #{deleteFlag} where id = #{id}")
    int updateStatusById(@Param("id") Long id, @Param("deleteFlag") boolean deleteFlag);

    /**
     * 根据数据集ID删除数据信息
     *
     * @param datasetId 数据集ID
     */
    @Delete("delete from data_dataset where  id = #{datasetId}")
    void deleteInfoById(@Param("datasetId") Long datasetId);

    List<DatasetVersionQueryVO> dataVersionListVO(@Param("annotateType") Integer annotateType,
                                                  @Param("module") Integer module, @Param("ids") List<Long> ids,
                                                  @Param("currentUserId") Long currentUserId);

}
