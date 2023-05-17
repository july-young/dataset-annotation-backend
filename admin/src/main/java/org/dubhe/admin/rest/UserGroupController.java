package org.dubhe.admin.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.domain.dto.UserGroupDTO;
import org.dubhe.admin.domain.dto.UserGroupDeleteDTO;
import org.dubhe.admin.domain.dto.UserGroupQueryDTO;
import org.dubhe.admin.domain.dto.UserGroupUpdDTO;
import org.dubhe.admin.domain.dto.UserRoleUpdateDTO;
import org.dubhe.admin.domain.dto.UserStateUpdateDTO;
import org.dubhe.admin.domain.entity.Group;
import org.dubhe.admin.domain.entity.User;
import org.dubhe.admin.domain.vo.UserGroupVO;
import org.dubhe.admin.service.GroupService;
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

@Api(tags = "系统：用户组管理")
@RestController
@RequestMapping("/group")
public class UserGroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    @ApiOperation("获取用户组列表")
    public DataResponseBody page(UserGroupQueryDTO queryDTO) {
        PageDTO<UserGroupVO> pageDTO= groupService.page(queryDTO);
        return new DataResponseBody(pageDTO);
    }

    @PostMapping
    @ApiOperation("创建用户组")
    @PreAuthorize(Permissions.USER_GROUP_CREATE)
    public DataResponseBody create(@Validated @RequestBody UserGroupDTO groupCreateDTO) {
        Group group = groupService.create(groupCreateDTO);
        return new DataResponseBody(group);
    }

    @PutMapping
    @ApiOperation("修改用户组信息")
    @PreAuthorize(Permissions.USER_GROUP_EDIT)
    public DataResponseBody update(@Validated @RequestBody UserGroupDTO groupUpdateDTO) {
        groupService.update(groupUpdateDTO);
        return new DataResponseBody();
    }

    @DeleteMapping
    @ApiOperation("删除用户组")
    @PreAuthorize(Permissions.USER_GROUP_DELETE)
    public DataResponseBody delete(@RequestBody UserGroupDeleteDTO groupDeleteDTO) {
        groupService.delete(groupDeleteDTO.getIds());
        return new DataResponseBody();
    }

    @PostMapping("/updateUser")
    @ApiOperation("修改组成员")
    @PreAuthorize(Permissions.USER_GROUP_EDIT_USER)
    public DataResponseBody updUserWithGroup(@Validated @RequestBody UserGroupUpdDTO userGroupUpdDTO) {
        groupService.updUserWithGroup(userGroupUpdDTO);
        return new DataResponseBody();
    }

    @DeleteMapping("/deleteUser")
    @ApiOperation("移除组成员")
    @PreAuthorize(Permissions.USER_GROUP_EDIT_USER)
    public DataResponseBody delUserWithGroup(@Validated @RequestBody UserGroupUpdDTO userGroupDelDTO) {
        groupService.delUserWithGroup(userGroupDelDTO);
        return new DataResponseBody();
    }

    @GetMapping("/findUser")
    @ApiOperation("获取没有归属组的用户")
    public DataResponseBody<List<User>> findUserWithOutGroup() {
        List<User> userList = groupService.findUserWithOutGroup();
        return new DataResponseBody(userList);
    }

    @GetMapping("/byGroupId")
    @ApiOperation("获取某个用户组的成员")
    public DataResponseBody<List<User>> queryUserByGroupId(Long groupId) {
        List<User> userList = groupService.queryUserByGroupId(groupId);
        return new DataResponseBody(userList);
    }

    @PutMapping("/userState")
    @ApiOperation("批量修改组用户的状态(激活/锁定)")
    @PreAuthorize(Permissions.USER_GROUP_EDIT_USER_STATE)
    public DataResponseBody updateUserState(@Validated @RequestBody UserStateUpdateDTO userStateUpdateDTO) {
        groupService.updateUserState(userStateUpdateDTO);
        return new DataResponseBody();
    }

    @DeleteMapping("/delete")
    @ApiOperation("批量删除组用户")
    @PreAuthorize(Permissions.USER_GROUP_DELETE_USER)
    public DataResponseBody delUser(@Validated @RequestBody UserGroupUpdDTO userGroupUpdDTO) {
        groupService.delUser(userGroupUpdDTO.getGroupId());
        return new DataResponseBody();
    }

    @PutMapping("/userRoles")
    @ApiOperation("批量修改组成员角色")
    @PreAuthorize(Permissions.USER_GROUP_EDIT_USER_ROLE)
    public DataResponseBody updateUserRole(@Validated @RequestBody UserRoleUpdateDTO userRoleUpdateDTO) {
        groupService.updateUserRole(userRoleUpdateDTO);
        return new DataResponseBody();
    }
}
