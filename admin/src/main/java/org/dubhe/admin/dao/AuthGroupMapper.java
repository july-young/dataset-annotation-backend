package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.dubhe.admin.domain.entity.*;

import java.util.List;
import java.util.Set;

/**
 * @description 权限组mapper接口
 * @date 2021-05-14
 */
public interface AuthGroupMapper extends BaseMapper<Auth> {


    /**
     * 根据角色id查询对应的权限组
     *
     * @param queryWrapper role的wrapper对象
     * @return 角色集合
     */
    @Select("select * from role ${ew.customSqlSegment}")
    @Results(id = "roleMapperResults",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "auths",
                            column = "id",
                            many = @Many(select = "org.dubhe.admin.dao.AuthGroupMapper.selectByRoleId", fetchType = FetchType.LAZY))})
    List<Role> selectCollList(@Param("ew") Wrapper<Role> queryWrapper);


    @Delete("<script>" +
            "delete from auth_permission where permission_id in" +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    void untiedByPermissionId(@Param("ids") Set<Long> ids);

    /**
     * 通过权限组id获取权限
     *
     * @param authId 权限组id
     * @return List<Permission> 权限列表
     */
    @Select("select * from permission where id in (select permission_id from auth_permission where auth_id=#{authId}) and deleted=0 order by id")
    List<Permission> getPermissionByAuthId(Long authId);

    /**
     * 根据角色id获取绑定的权限组列表
     *
     * @param roleId 角色id
     * @return 权限组列表
     */
    @Select("select * from auth_group  where id in (select auth_id from roles_auth where role_id=#{roleId}) and deleted=0")
    List<Auth> selectByRoleId(long roleId);

}
