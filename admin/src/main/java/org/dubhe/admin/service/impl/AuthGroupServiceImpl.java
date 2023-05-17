/**
 * Copyright 2020 Tianshu AI Platform. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================================================
 */
package org.dubhe.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dubhe.admin.dao.AuthGroupMapper;
import org.dubhe.admin.dao.AuthPermissionMapper;
import org.dubhe.admin.dao.PermissionMapper;
import org.dubhe.admin.dao.RoleAuthMapper;
import org.dubhe.admin.domain.dto.AuthCodeCreateDTO;
import org.dubhe.admin.domain.dto.AuthCodeQueryDTO;
import org.dubhe.admin.domain.dto.AuthCodeUpdateDTO;
import org.dubhe.admin.domain.dto.RoleAuthUpdateDTO;
import org.dubhe.admin.domain.entity.Auth;
import org.dubhe.admin.domain.entity.AuthPermission;
import org.dubhe.admin.domain.entity.Permission;
import org.dubhe.admin.domain.entity.RoleAuth;
import org.dubhe.admin.domain.vo.AuthVO;
import org.dubhe.admin.service.AuthGroupService;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description 权限组服务实现类
 * @date 2021-05-14
 */
@Service
public class AuthGroupServiceImpl extends ServiceImpl<AuthGroupMapper, Auth> implements AuthGroupService {

    @Autowired
    private AuthGroupMapper authGroupMapper;

    @Autowired
    private RoleAuthMapper roleAuthMapper;

    @Autowired
    private AuthPermissionMapper authPermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserContextService userContextService;

    public final static List<String> FIELD_NAMES;

    static {
        FIELD_NAMES = ReflectionUtils.getFieldNames(AuthVO.class);
    }

    /**
     * 分页查询权限组信息
     *
     * @param authCodeQueryDTO 分页查询实例
     * @return Map<String, Object> 权限组分页信息
     */
    @Override
    public PageDTO<AuthVO> page(AuthCodeQueryDTO authCodeQueryDTO) {

        Page page = authCodeQueryDTO.toPage();
        QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(authCodeQueryDTO.getAuthCode())) {
            queryWrapper.eq("id", authCodeQueryDTO.getAuthCode())
                    .or()
                    .like("auth_code", authCodeQueryDTO.getAuthCode());
        }

        //排序
        if (FIELD_NAMES.contains(authCodeQueryDTO.getSort())) {
            boolean asc = StringConstant.SORT_ASC.equalsIgnoreCase(authCodeQueryDTO.getOrder());
            queryWrapper.orderBy(true, asc, StringUtils.humpToLine(authCodeQueryDTO.getSort()));
        } else {
            queryWrapper.orderByDesc(StringConstant.ID);
        }

        IPage<Auth> groupList = authGroupMapper.selectPage(page, queryWrapper);
        List<AuthVO> authResult = groupList.getRecords().stream().map(x -> {
            AuthVO authVO = new AuthVO();
            BeanUtils.copyProperties(x, authVO);
            List<Permission> permissions = authGroupMapper.getPermissionByAuthId(x.getId());
            authVO.setPermissions(permissions);
            return authVO;
        }).collect(Collectors.toList());
        return PageUtil.toPage(page, authResult);
    }

    /**
     * 创建权限组
     *
     * @param authCodeCreateDTO 创建权限组DTO实例
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(AuthCodeCreateDTO authCodeCreateDTO) {
        Auth auth = new Auth();
        BeanUtil.copyProperties(authCodeCreateDTO, auth);
        checkAuthCodeIsExist(auth);
        authGroupMapper.insert(auth);
        tiedWithPermission(auth.getId(), authCodeCreateDTO.getPermissions());
    }


    /**
     * 修改权限组信息
     *
     * @param authCodeUpdateDTO 修改权限组信息DTO实例
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(AuthCodeUpdateDTO authCodeUpdateDTO) {
        //获取当前用户id
        Long curUserId = userContextService.getCurUserId();
        Auth auth = new Auth();
        BeanUtil.copyProperties(authCodeUpdateDTO, auth);
        checkAuthCodeIsExist(auth);
        auth.setUpdateUserId(curUserId);
        authGroupMapper.updateById(auth);
        if (CollUtil.isNotEmpty(authCodeUpdateDTO.getPermissions())) {
            untiedWithPermission(authCodeUpdateDTO.getId());
            tiedWithPermission(auth.getId(), authCodeUpdateDTO.getPermissions());
        }
    }

    /**
     * 批量删除权限组
     *
     * @param ids 权限组id集合
     */
    @Override
    public void delete(Set<Long> ids) {
        //删除权限组数据
        removeByIds(ids);
        LambdaQueryWrapper<AuthPermission> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(AuthPermission::getAuthId, ids);
        authPermissionMapper.delete(lambdaQueryWrapper);
    }


    private void tiedWithPermission(Long authId, Set<Long> permissionIds) {
        List<AuthPermission> list = new ArrayList<>();
        for (Long id : permissionIds) {
            AuthPermission authPermission = new AuthPermission();
            authPermission.setAuthId(authId);
            authPermission.setPermissionId(id);
            list.add(authPermission);
        }
        authPermissionMapper.insertBatchSomeColumn(list);
    }

    private void untiedWithPermission(Long authId) {
        LambdaQueryWrapper<AuthPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AuthPermission::getAuthId, authId);
        authPermissionMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 修改角色权限
     *
     * @param roleAuthUpdateDTO
     * @return
     */
    @Override
    public void updateRoleAuth(RoleAuthUpdateDTO roleAuthUpdateDTO) {
        LambdaQueryWrapper<RoleAuth> lambdaUpdateWrapper = new LambdaQueryWrapper<>();
        lambdaUpdateWrapper.eq(RoleAuth::getRoleId, roleAuthUpdateDTO.getRoleId());
        roleAuthMapper.delete(lambdaUpdateWrapper);

        List<RoleAuth> roleAuths = new ArrayList<>();
        if (CollUtil.isNotEmpty(roleAuthUpdateDTO.getAuthIds())) {
            for (Long authId : roleAuthUpdateDTO.getAuthIds()) {
                RoleAuth roleAuth = new RoleAuth();
                roleAuth.setRoleId(roleAuthUpdateDTO.getRoleId());
                roleAuth.setAuthId(authId);
                roleAuths.add(roleAuth);
            }
            roleAuthMapper.insertBatchSomeColumn(roleAuths);
        }
    }

    /**
     * 获取权限组tree
     *
     * @return List<Auth> 权限组tree
     */
    @Override
    public List<AuthVO> getAuthCodeList() {
        List<Auth> authList = authGroupMapper.selectList(new LambdaQueryWrapper<>());
        List<AuthVO> resultList = new ArrayList<>();
        for (Auth auth : authList) {
            AuthVO authVO = new AuthVO();
            BeanUtils.copyProperties(auth, authVO);
            List<Permission> permissions = authGroupMapper.getPermissionByAuthId(authVO.getId());
            authVO.setPermissions(permissions);
            resultList.add(authVO);
        }
        return resultList;
    }

    /**
     * 根据authCode获取权限组
     *
     * @param auth 权限组名称
     * @return List<Auth> 权限组列表
     */
    private void checkAuthCodeIsExist(Auth auth) {
        LambdaQueryWrapper<Auth> authLambdaQueryWrapper = new LambdaQueryWrapper<>();
        authLambdaQueryWrapper.eq(Auth::getAuthCode, auth.getAuthCode());
        Integer count = authGroupMapper.selectCount(authLambdaQueryWrapper);
        if (count > 0) {
            throw new BusinessException("权限组名称不能重复");
        }
    }
}
