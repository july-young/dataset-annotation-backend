
package org.dubhe.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.DictDetail;
import org.dubhe.biz.base.dto.DictDetailDTO;
import org.dubhe.biz.db.utils.PageDTO;

import java.util.List;
import java.util.Set;

/**
 * @description 字典详情服务 Service
 * @date 2020-06-01
 */
public interface DictDetailService {

    /**
     * 根据ID查询
     *
     * @param id 字典Id
     * @return 字典详情DTO
     */
    DictDetailDTO findById(Long id);

    /**
     * 创建
     *
     * @param resources 字典详情创建实体
     * @return 字典详情实体
     */
    DictDetailDTO create(DictDetailCreateDTO resources);

    /**
     * 编辑
     *
     * @param resources 字典详情修改实体
     */
    void update(DictDetailUpdateDTO resources);

    /**
     * 删除
     *
     * @param ids 字典详情ids
     */
    void delete(Set<Long> ids);

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return 字典分页列表数据
     */
    PageDTO page(DictDetailQueryDTO criteria, Page<DictDetail> page);

    /**
     * 查询全部数据
     */
    List<DictDetailDTO> list(DictDetailQueryDTO criteria);

    /**
     * 根据名称查询字典详情
     */
    List<DictDetailDTO> getDictDetailByLabel(String label );
}
