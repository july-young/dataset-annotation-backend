package org.dubhe.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.dto.RoleDTO;
import org.dubhe.admin.domain.entity.Role;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @description 角色服务 Service

 */
public interface RoleService extends IService<Role> {

    /**
     * 根据ID查询角色信息
     *
     * @param id id
     * @return org.dubhe.domain.dto.RoleDTO 角色信息
     */
    RoleDTO findById(long id);

    /**
     * 新增角色
     *
     * @param resources 角色新增请求实体
     * @return org.dubhe.domain.dto.RoleDTO 角色返回实例
     */
    RoleDTO create(RoleCreateDTO resources);

    /**
     * 修改角色
     *
     * @param resources 角色修改请求实体
     * @return void
     */
    void update(RoleUpdateDTO resources);

    /**
     * 批量删除角色
     *
     * @param ids 角色ids
     */
    void delete(Set<Long> ids);


    /**
     * 修改角色菜单
     *
     * @param resources 角色菜单请求实体
     * @param roleDTO   角色请求实体
     */
    void updateMenu(RoleUpdateDTO resources, RoleDTO roleDTO);


    /**
     * 删除角色菜单
     */
    void untiedMenu(Collection<Long> ids);

    /**
     * 按条件查询角色信息
     *
     * @param criteria 角色查询条件
     * @return java.util.List<org.dubhe.domain.dto.RoleSmallDTO> 角色信息返回实例
     */
    List<RoleSmallDTO> queryAllSmall(RoleQueryDTO criteria);

    /**
     * 分页查询角色列表
     *
     * @param criteria 角色查询条件
     * @param page     分页实体
     * @return java.lang.Object 角色信息返回实例
     */
    Object queryAll(RoleQueryDTO criteria, Page page);

    /**
     * 按条件查询角色列表
     *
     * @param criteria 角色查询条件
     * @return java.util.List<org.dubhe.domain.dto.RoleDTO> 角色信息返回实例
     */
    List<RoleDTO> queryAll(RoleQueryDTO criteria);

    /**
     * 导出角色信息
     *
     * @param roles    角色列表
     * @param response 导出http响应
     */
    void download(List<RoleDTO> roles, HttpServletResponse response) throws IOException;

    /**
     * 根据用户ID获取角色信息
     *
     * @param userId 用户ID
     * @return java.util.List<org.dubhe.domain.dto.RoleSmallDTO> 角色列表
     */
    List<RoleSmallDTO> getRoleByUserId( Long userId);

    /**
     * 新增角色菜单
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    void tiedRoleMenu(Long roleId, Long menuId);
}
