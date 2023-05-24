
package org.dubhe.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dubhe.admin.domain.dto.DictCreateDTO;
import org.dubhe.admin.domain.dto.DictDTO;
import org.dubhe.admin.domain.dto.DictQueryDTO;
import org.dubhe.admin.domain.dto.DictUpdateDTO;
import org.dubhe.admin.domain.entity.Dict;
import org.dubhe.biz.db.utils.PageDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @description 字典服务 Service

 */
public interface DictService {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageDTO<DictDTO> page(DictQueryDTO criteria, Page<Dict> page);

    /**
     * 按条件查询字典列表
     *
     * @param criteria 字典查询实体
     * @return java.util.List<org.dubhe.domain.dto.DictDTO> 字典实例
     */
    List<DictDTO> list(DictQueryDTO criteria);

    /**
     * 通过ID查询字典详情
     *
     * @param id 字典ID
     * @return org.dubhe.domain.dto.DictDTO 字典实例
     */
    DictDTO findById(Long id);

    /**
     * 通过Name查询字典详情
     *
     * @param name 字典名称
     * @return org.dubhe.domain.dto.DictDTO 字典实例
     */
    DictDTO findByName(String name);

    /**
     * 新增字典
     *
     * @param resources 字典新增实体
     * @return org.dubhe.domain.dto.DictDTO 字典实例
     */
    DictDTO create(DictCreateDTO resources);

    /**
     * 字典修改
     *
     * @param resources 字典修改实体
     */
    void update(DictUpdateDTO resources);

    /**
     * 字典批量删除
     *
     * @param ids 字典ID
     */
    void deleteAll(Set<Long> ids);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response 导出http响应
     * @throws IOException 导出异常
     */
    void download(List<DictDTO> queryAll, HttpServletResponse response) throws IOException;
}
