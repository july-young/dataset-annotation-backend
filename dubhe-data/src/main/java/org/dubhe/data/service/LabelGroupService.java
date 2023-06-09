

package org.dubhe.data.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.biz.base.vo.LabelGroupBaseVO;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.data.domain.dto.GroupConvertPresetDTO;
import org.dubhe.data.domain.dto.LabelGroupCopyDTO;
import org.dubhe.data.domain.dto.LabelGroupCreateDTO;
import org.dubhe.data.domain.dto.LabelGroupDeleteDTO;
import org.dubhe.data.domain.dto.LabelGroupImportDTO;
import org.dubhe.data.domain.dto.LabelGroupQueryDTO;
import org.dubhe.data.domain.entity.LabelGroup;
import org.dubhe.data.domain.vo.LabelGroupQueryVO;
import org.dubhe.data.domain.vo.LabelGroupVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 标签组服务

 */
public interface LabelGroupService {

    /**
     * 创建标签组
     *
     * @param labelGroupCreateDTO 创建标签组DTO
     */
    Long creatLabelGroup(LabelGroupCreateDTO labelGroupCreateDTO);

    /**
     * 更新（编辑）标签组
     *
     * @param labelGroupId        标签组ID
     * @param labelGroupCreateDTO 创建标签组DTO
     * @return Boolean 是否更新成功
     */
    void update(Long labelGroupId, LabelGroupCreateDTO labelGroupCreateDTO);

    /**
     * 删除标签组
     */
    void delete(DeleteDTO deleteDTO);

    /**
     * 删除标签组方法
     *
     * @param labelGroupId 标签组ID
     */
    void delete(Long labelGroupId);

    /**
     * 标签组分页列表
     */
    PageDTO<LabelGroupQueryVO> page(Page<LabelGroup> page, LabelGroupQueryVO labelGroupQueryVO);

    /**
     * 标签组详情
     *
     * @param labelGroupId   标签组id
     * @return LabelGroupVO  根据Id查询出对应的标签组
     */
    LabelGroupVO get(Long labelGroupId);

    /**
     * 标签组列表
     *
     * @param labelGroupQueryDTO 查询条件
     * @return List<LabelGroup> 查询出对应的标签组
     */
    List<LabelGroup> getList(LabelGroupQueryDTO labelGroupQueryDTO);

    /**
     * 导入标签组
     *
     * @param labelGroupImportDTO 标签组导入DTO
     * @param file                导入文件
     */
    Long importLabelGroup(LabelGroupImportDTO labelGroupImportDTO, MultipartFile file);

    /**
     * 标签组复制
     *
     * @param labelGroupCopyDTO 标签组复制DTO
     */
    void copy(LabelGroupCopyDTO labelGroupCopyDTO);

    /**
     * 根据标签组ID 校验是否能自动标注
     *
     * @param labelGroupId  标签组
     * @return      true: 能  false: 否
     */
    boolean isAnnotationByGroupId(Long labelGroupId);

    /**
     * 普通标签组转预置
     *
     * @param groupConvertPresetDTO 普通标签组转预置请求实体
     */
    void convertPreset(GroupConvertPresetDTO groupConvertPresetDTO);

    /**
     * 根据标签组ID查询标签组数据
     *
     * @param groupId 标签组ID
     */
    void deleteByGroupId(Long groupId);

    /**
     * 根据标签组ID修改状态
     *
     * @param groupId 标签组ID
     * @param deletedFlag 删除标识
     */
    void updateStatusByGroupId(Long groupId, Boolean deletedFlag);

    /**
     * 根据标签组id获取标签组基本信息
     * @param labelGroupIds 标签组ID集合
     * @return LabelGroupBaseVO
     */
    List<LabelGroupBaseVO> queryLabelGroupList(Set<Long> labelGroupIds);
}
