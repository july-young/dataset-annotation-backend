package org.dubhe.admin.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.vo.PermissionVO;
import org.dubhe.admin.service.PermissionService;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.db.utils.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "系统：操作权限")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("返回全部的操作权限")
    @GetMapping(value = "/tree")
    public DataResponseBody getPermissionTree() {
        List<PermissionTreeDTO> permissionTree = permissionService.getPermissionTree(0L);
        return new DataResponseBody(permissionTree);
    }

    @ApiOperation("查询权限")
    @GetMapping
    @PreAuthorize(Permissions.AUTH_CODE)
    public DataResponseBody<PageDTO<PermissionVO>> queryAll(PermissionQueryDTO queryDTO) {
        PageDTO<PermissionVO> permissionVOPageDTO = permissionService.queryAll(queryDTO);
        return new DataResponseBody(permissionVOPageDTO);
    }

    @ApiOperation("新增权限")
    @PostMapping
    @PreAuthorize(Permissions.PERMISSION_CREATE)
    public DataResponseBody create(@Validated @RequestBody PermissionCreateDTO permissionCreateDTO) {
        permissionService.create(permissionCreateDTO);
        return new DataResponseBody();
    }

    @ApiOperation("修改权限")
    @PutMapping
    @PreAuthorize(Permissions.PERMISSION_EDIT)
    public DataResponseBody update(@Validated @RequestBody PermissionUpdateDTO permissionUpdateDTO) {
        permissionService.update(permissionUpdateDTO);
        return new DataResponseBody();
    }

    @ApiOperation("删除权限")
    @DeleteMapping
    @PreAuthorize(Permissions.PERMISSION_DELETE)
    public DataResponseBody delete(@RequestBody PermissionDeleteDTO permissionDeleteDTO) {
        permissionService.delete(permissionDeleteDTO);
        return new DataResponseBody();
    }


}
