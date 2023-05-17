package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.dubhe.admin.dao.provider.MenuProvider;
import org.dubhe.admin.domain.entity.Menu;
import org.dubhe.biz.base.dto.SysPermissionDTO;

import java.util.List;
import java.util.Set;

/**
 * @description 菜单 mapper
 * @date 2020-04-02
 */
public interface MenuMapper extends BaseMapper<Menu> {


    /**
     * 根据角色id查询用户权限
     *
     * @param roleIds 用户id
     * @return 权限信息
     */
    List<SysPermissionDTO> selectPermissionByRoleIds(@Param("list") List<Long> roleIds);


    /**
     * 根据组件名称查询
     *
     * @param name 组件名称
     * @return 菜单对象
     */
    @Select("select * from menu where  component=#{name}  and deleted = 0 ")
    Menu findByComponentName(String name);

    /**
     * 根据菜单的 PID 查询
     *
     * @param pid 菜单pid
     * @return 菜单列表
     */
    @Select("select * from menu where  pid=#{pid} and deleted = 0 ")
    List<Menu> findByPid(long pid);

    /**
     * 根据角色ID与菜单类型查询菜单
     *
     * @param roleIds roleIDs
     * @param type    类型
     * @return 菜单列表
     */
    @SelectProvider(type = MenuProvider.class, method = "findByRolesIdInAndTypeNotOrderBySortAsc")
    List<Menu> findByRolesIdInAndTypeNotOrderBySortAsc(Set<Long> roleIds, int type);


    /**
     * 根据角色ID与查询菜单
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    @Select("select m.* from menu m,roles_menus rm where m.id= rm.menu_id and rm.role_id=#{roleId}  and deleted = 0 ")
    List<Menu> selectByRoleId(long roleId);

}
