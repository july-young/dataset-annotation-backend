
package org.dubhe.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.Permission;
import org.dubhe.admin.domain.vo.PermissionVO;
import org.dubhe.biz.db.utils.PageDTO;

import java.util.List;
import java.util.Map;

/**
 * @description 操作权限服务
 * @date 2021-04-28
 */
public interface PermissionService extends IService<Permission> {


    /**
     * 根据ID获取权限列表
     *
     * @param pid id
     * @return java.util.List<org.dubhe.domain.entity.Permission> 权限返回列表
     */
    List<Permission> findByPid(long pid);

    /**
     * 获取权限树
     */
    List<PermissionTreeDTO> getPermissionTree( Long pid);


    /**
     * 获取权限列表
     */
    PageDTO<PermissionVO> queryAll(PermissionQueryDTO permissionQueryDTO);

    /**
     * 新增权限
     *
     * @param permissionCreateDTO 新增权限DTO
     */
    void create(PermissionCreateDTO permissionCreateDTO);

    /**
    * 修改权限
    *
    * @param permissionUpdateDTO 修改权限DTO
    */
    void update(PermissionUpdateDTO permissionUpdateDTO);

    /**
     * 删除权限
     *
     * @param permissionDeleteDTO 删除权限DTO
     */
    void delete(PermissionDeleteDTO permissionDeleteDTO);


}
