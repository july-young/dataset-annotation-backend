package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dubhe.admin.domain.entity.UserRole;

import java.util.List;
import java.util.Set;

/**
 * @description 用户角色 mapper

 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 批量删除用户
     *
     * @param userIds 用户id集合
     */
    void deleteByUserId(@Param("list") Set<Long> userIds);

    /**
     * 批量添加用户角色
     *
     * @param userRoles 用户角色实体集合
     */
    void insertBatchs(List<UserRole> userRoles);

}

