package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dubhe.admin.domain.entity.Group;
import org.dubhe.admin.domain.entity.User;

import java.util.List;

/**
 * @description 用户组mapper接口
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     *  获取用户组成员信息
     *
     * @param groupId 用户组id
     * @return List<User> 用户列表
     */
    @Select("select u.* from user u left join user_group gu on u.id = gu.user_id where gu.group_id=#{groupId} and deleted=0 ")
    List<User> queryUserByGroupId(Long groupId);

    /**
     * 获取还未分组的用户
     *
     * @return List<User> 用户组成员列表
     */
    @Select("select * from user where id not in (select user_id from user_group) and deleted=0")
    List<User> findUserWithOutGroup();

}
