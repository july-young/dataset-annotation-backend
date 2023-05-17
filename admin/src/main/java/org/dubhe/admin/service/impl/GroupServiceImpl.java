package org.dubhe.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import org.dubhe.admin.dao.GroupMapper;
import org.dubhe.admin.dao.UserGroupMapper;
import org.dubhe.admin.dao.UserRoleMapper;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.Group;
import org.dubhe.admin.domain.entity.User;
import org.dubhe.admin.domain.entity.UserGroup;
import org.dubhe.admin.domain.entity.UserRole;
import org.dubhe.admin.domain.vo.UserGroupVO;
import org.dubhe.admin.service.GroupService;
import org.dubhe.admin.service.UserService;
import org.dubhe.biz.base.constant.StringConstant;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.service.UserContextService;
import org.dubhe.biz.base.utils.ReflectionUtils;
import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.db.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public final static List<String> FIELD_NAMES;

    static {
        FIELD_NAMES = ReflectionUtils.getFieldNames(UserGroupVO.class);
    }


    /**
     * 分页查询用户组列表
     */
    @Override
    public PageDTO<UserGroupVO> page(UserGroupQueryDTO queryDTO) {

        Page page = queryDTO.toPage();
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(queryDTO.getKeyword())){
            queryWrapper.eq( "id", queryDTO.getKeyword())
                    .or()
                    .like("name", queryDTO.getKeyword());
        }

        //排序
        if (queryDTO.getSort() != null && FIELD_NAMES.contains(queryDTO.getSort())) {
            boolean asc = StringConstant.SORT_ASC.equalsIgnoreCase(queryDTO.getOrder());
            queryWrapper.orderBy(true, asc, StringUtils.humpToLine(queryDTO.getSort()));
        } else {
            queryWrapper.orderByDesc(StringConstant.ID);
        }
        IPage<Group> groupList = groupMapper.selectPage(page, queryWrapper);
        List<UserGroupVO> userGroupResult = groupList.getRecords().stream().map(x -> {
            UserGroupVO userGroupVO = new UserGroupVO();
            BeanUtils.copyProperties(x, userGroupVO);
            return userGroupVO;
        }).collect(Collectors.toList());
        return PageUtil.toPage(page, userGroupResult);
    }

    /**
     * 新增用户组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Group create(UserGroupDTO groupCreateDTO) {
        Group userGroup = new Group();
        checkNameUnique(groupCreateDTO.getName());
        BeanUtils.copyProperties(groupCreateDTO, userGroup);
        groupMapper.insert(userGroup);
        return userGroup;
    }

    private void checkNameUnique(String name) {
        LambdaQueryWrapper<Group> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Group::getName, name);
        Integer count = groupMapper.selectCount(lambdaQueryWrapper);
        if (count > 0) {
            throw new BusinessException("用户组名称不能重复");
        }
    }

    /**
     * 修改用户组信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserGroupDTO groupUpdateDTO) {
        Group group = groupMapper.selectById(groupUpdateDTO.getId());
        if(group==null){
            throw new BusinessException("无此用户组。");
        }
        if(!group.getName().equals(groupUpdateDTO.getName())){
            checkNameUnique(groupUpdateDTO.getName());
        }
        Group userGroup = new Group();
        BeanUtils.copyProperties(groupUpdateDTO, userGroup);
        groupMapper.updateById(userGroup);

    }

    /**
     * 删除用户组
     *
     * @param ids 用户组ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        deleteUserGroupByGroupIds(ids);
        groupMapper.deleteBatchIds(ids);
    }

    private void deleteUserGroupByGroupIds(Set<Long> groupIds) {
        LambdaQueryWrapper<UserGroup> userGroupQueryWrapper = new LambdaQueryWrapper<>();
        userGroupQueryWrapper.in(UserGroup::getGroupId, groupIds);
        userGroupMapper.delete(userGroupQueryWrapper);
    }

    private void deleteUserGroup(Long userId, Long groupId) {
        LambdaQueryWrapper<UserGroup> userGroupQueryWrapper = new LambdaQueryWrapper<>();
        userGroupQueryWrapper.eq(UserGroup::getUserId, userId);
        userGroupQueryWrapper.eq(UserGroup::getGroupId, groupId);
        userGroupMapper.delete(userGroupQueryWrapper);
    }

    /**
     * 修改用户组成员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updUserWithGroup(UserGroupUpdDTO userGroupUpdDTO) {
        Long groupId = userGroupUpdDTO.getGroupId();
        if (groupId != null) {
            deleteUserGroupByGroupIds(Sets.newHashSet(groupId));
            for (Long userId : userGroupUpdDTO.getUserIds()) {
                userGroupMapper.insert(new UserGroup(userId, groupId));
            }
        }
    }

    /**
     * 删除用户组成员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUserWithGroup(UserGroupUpdDTO userGroupDelDTO) {
        for (Long userId : userGroupDelDTO.getUserIds()) {
            deleteUserGroup(userId, userGroupDelDTO.getGroupId());
        }
    }


    /**
     * 获取没有归属组的用户
     *
     * @return List<User> 没有归属组的用户
     */
    @Override
    public List<User> findUserWithOutGroup() {
        return groupMapper.findUserWithOutGroup();
    }


    /**
     * 获取用户组成员信息
     *
     * @param groupId 用户组id
     * @return List<User> 用户列表
     */
    @Override
    public List<User> queryUserByGroupId(Long groupId) {
        return groupMapper.queryUserByGroupId(groupId);
    }

    /**
     * 批量修改用户组成员的状态
     *
     * @param userStateUpdateDTO 实体DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserState(UserStateUpdateDTO userStateUpdateDTO) {
        //获取用户组的成员id
        List<User> userList = groupMapper.queryUserByGroupId(userStateUpdateDTO.getGroupId());
        Set<Long> ids = new HashSet<>();
        if (CollUtil.isNotEmpty(userList)) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(User::getId, ids);
        updateWrapper.set(User::getEnabled, userStateUpdateDTO.isEnabled());
        userService.update(updateWrapper);
    }

    /**
     * 批量删除用户组用户
     */
    @Override
    public void delUser(Long groupId) {
        deleteUserGroupByGroupIds(Sets.newHashSet(groupId));
        //获取用户组的成员id
        List<User> userList = groupMapper.queryUserByGroupId(groupId);
        Set<Long> ids = new HashSet(userList);
        userService.delete(ids);
    }

    /**
     * 批量修改用户组用户的角色
     *
     * @param userRoleUpdateDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(UserRoleUpdateDTO userRoleUpdateDTO) {
        //获取用户组的成员id
        List<User> userList = groupMapper.queryUserByGroupId(userRoleUpdateDTO.getGroupId());
        List<UserRole> userRoleList = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        if (CollUtil.isNotEmpty(userList)) {
            for (User user : userList) {
                ids.add(user.getId());
                for (Long roleId : userRoleUpdateDTO.getRoleIds()) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleId);
                    userRoleList.add(userRole);
                }
            }
        }
        //清空当前用户
        userRoleMapper.deleteByUserId(ids);
        //添加用户的新角色
        userRoleMapper.insertBatchs(userRoleList);
    }
}
