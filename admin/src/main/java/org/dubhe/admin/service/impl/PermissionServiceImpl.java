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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dubhe.admin.dao.AuthGroupMapper;
import org.dubhe.admin.dao.PermissionMapper;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.Permission;
import org.dubhe.admin.domain.vo.PermissionVO;
import org.dubhe.admin.service.PermissionService;
import org.dubhe.admin.service.convert.PermissionConvert;
import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.service.UserContextService;
import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.db.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description 操作权限服务实现类
 * @date 2021-04-28
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private PermissionConvert permissionConvert;

    @Autowired
    private AuthGroupMapper authGroupMapper;

    /**
     * 获取权限列表
     */
    @Override
    public List<Permission> findByPid(long pid) {
        return permissionMapper.findByPid(pid);
    }

    public List<PermissionTreeDTO> getPermissionTree(Long pid) {
        List<Permission> permissions = permissionMapper.selectList(new QueryWrapper<>());
        return getPermissionTree(permissions, pid);
    }

    /**
     * 获取权限树
     */
    private List<PermissionTreeDTO> getPermissionTree(List<Permission> permissions, Long pid) {
        List<PermissionTreeDTO> list = new LinkedList<>();
        for (Permission permission : permissions) {
            if (permission != null && pid.equals(permission.getPid())) {
                PermissionTreeDTO dto = new PermissionTreeDTO();
                dto.setId(permission.getId());
                dto.setPermission(permission.getPermission());
                dto.setLabel(permission.getName());
                dto.setChildren(getPermissionTree(permissions, permission.getId()));
                list.add(dto);
            }
        }
        return list;
    }

    /**
     * 获取权限列表
     *
     * @param permissionQueryDTO 权限查询DTO
     * @return Map<String, Object>
     */
    @Override
    public PageDTO<PermissionVO> queryAll(PermissionQueryDTO permissionQueryDTO) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(permissionQueryDTO.getKeyword())) {
            queryWrapper.and(x -> x.like(Permission::getName, permissionQueryDTO.getKeyword()).or().like(Permission::getPermission, permissionQueryDTO.getKeyword()));
        }
        queryWrapper.orderByDesc(Permission::getCreateTime);
        List<Permission> permissions = permissionMapper.selectList(queryWrapper);
        return buildTree(permissionConvert.toDto(permissions));
    }

    private PageDTO<PermissionVO> buildTree(List<PermissionVO> permissions) {
        List<PermissionVO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (PermissionVO permissionVO : permissions) {
            if (permissionVO.getPid() == 0) {
                trees.add(permissionVO);
            }
            for (PermissionVO vo : permissions) {
                if (vo.getPid().equals(permissionVO.getId())) {
                    if (CollUtil.isEmpty(permissionVO.getChildren())) {
                        permissionVO.setChildren(new ArrayList<>());
                    }
                    permissionVO.getChildren().add(vo);
                    ids.add(vo.getId());
                }
            }

        }

        if (trees.size() == 0) {
            permissions.stream().filter(x -> !ids.contains(x.getId())).collect(Collectors.toList());
        }
        return PageUtil.toPage(new Page(1, permissions.size()), trees);
    }

    /**
     * 新增权限
     *
     * @param permissionCreateDTO 新增权限DTO
     */
    @Override
    public void create(PermissionCreateDTO permissionCreateDTO) {
        UserContext curUser = userContextService.getCurUser();
        List<Permission> permissions = new ArrayList<>();
        for (Permission resource : permissionCreateDTO.getPermissions()) {
            if (permissionMapper.findByName(resource.getName()) != null) {
                throw new BusinessException("权限名称已存在");
            }

            Permission permission = new Permission();
            permission.setPid(permissionCreateDTO.getPid())
                    .setName(resource.getName())
                    .setPermission(resource.getPermission())
                    .setCreateUserId(curUser.getId());
            permissions.add(permission);
        }
        saveBatch(permissions);
    }

    /**
     * 修改权限
     *
     * @param permissionUpdateDTO 修改权限DTO
     */
    @Override
    public void update(PermissionUpdateDTO permissionUpdateDTO) {
        UserContext curUser = userContextService.getCurUser();
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionUpdateDTO, permission);
        for (Permission per : permissionUpdateDTO.getPermissions()) {
            permission.setName(per.getName());
            permission.setPermission(per.getPermission());
            permission.setUpdateUserId(curUser.getId());
            permissionMapper.updateById(permission);
        }
    }

    /**
     * 删除权限
     */
    @Override
    public void delete(PermissionDeleteDTO permissionDeleteDTO) {
        Set<Long> ids = new HashSet<>(permissionDeleteDTO.getIds());
        List<Permission> permissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>().in(Permission::getId, permissionDeleteDTO.getIds()));
        Queue<Permission> queue= new LinkedList(permissions);
        while (!queue.isEmpty()){
            Permission permission = queue.poll();
            List<Permission> permissionList = permissionMapper.findByPid(permission.getId());
            permissionList.forEach(x -> {
                ids.add(x.getId());
                queue.add(x);
            });
        }
        //解绑权限组权限
        authGroupMapper.untiedByPermissionId(ids);
        permissionMapper.deleteBatchIds(ids);
    }
}
