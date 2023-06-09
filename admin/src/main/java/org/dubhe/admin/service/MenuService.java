package org.dubhe.admin.service;

import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.Menu;
import org.dubhe.admin.domain.vo.MenuVo;
import org.dubhe.biz.db.utils.PageDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 菜单服务 Service

 */
public interface MenuService {

    /**
     * 按条件查询菜单列表
     *
     * @param criteria 菜单请求实体
     * @return java.util.List<org.dubhe.domain.dto.MenuDTO> 菜单返回实例
     */
    List<MenuDTO> queryAll(MenuQueryDTO criteria);

    /**
     * 根据id查询菜单信息
     *
     * @param id 菜单id
     * @return org.dubhe.domain.dto.MenuDTO 菜单返回实例
     */
    MenuDTO findById(long id);

    /**
     * 新增菜单
     *
     * @param resources 菜单新增请求实体
     * @return org.dubhe.domain.dto.MenuDTO 菜单返回实例
     */
    MenuDTO create(MenuCreateDTO resources);

    /**
     * 修改菜单
     *
     * @param resources 菜单修改请求实体
     */
    void update(MenuUpdateDTO resources);

    void delete(Set<Long> ids);

    /**
     * 获取菜单树
     */
    List<MenuTreeDTO> getMenuTree();

    /**
     * 根据ID获取菜单列表
     *
     * @param pid id
     * @return java.util.List<org.dubhe.domain.entity.Menu> 菜单返回列表
     */
    List<Menu> findByPid(long pid);

    /**
     * 构建菜单树
     *
     * @param menuDtos 菜单请求实体
     * @return java.util.Map<java.lang.String, java.lang.Object>  菜单树结构
     */
    PageDTO<MenuDTO> buildTree(List<MenuDTO> menuDtos);

    /**
     * 根据角色查询菜单列表
     *
     * @param roles 角色
     * @return java.util.List<org.dubhe.domain.dto.MenuDTO> 菜单返回实例
     */
    List<MenuDTO> findByRoles(List<RoleSmallDTO> roles);

    /**
     * 构建菜单树
     */
    List<MenuVo> buildMenus(List<MenuDTO> menuDTOList);

    /**
     * 获取菜单
     *
     * @param id id
     * @return org.dubhe.domain.entity.Menu 菜单返回实例
     */
    Menu findOne(Long id);

    /**
     * 导出
     *
     * @param queryAll 待导出的数据
     * @param response 导出http响应
     * @throws IOException 导出异常
     */
    void download(List<MenuDTO> queryAll, HttpServletResponse response) throws IOException;
}
