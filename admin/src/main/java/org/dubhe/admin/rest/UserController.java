package org.dubhe.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.domain.dto.UserConfigDTO;
import org.dubhe.admin.domain.dto.UserCreateDTO;
import org.dubhe.admin.domain.dto.UserDeleteDTO;
import org.dubhe.admin.domain.dto.UserQueryDTO;
import org.dubhe.admin.domain.dto.UserUpdateDTO;
import org.dubhe.admin.service.UserService;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.dto.UserDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.db.utils.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @description 用户管理

 */
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize(Permissions.USER_DOWNLOAD)
    public void download(HttpServletResponse response, UserQueryDTO criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @ApiOperation("查询用户")
    @GetMapping
    public DataResponseBody getUsers(UserQueryDTO queryDTO, Page page) {
        PageDTO<UserDTO> userDTOPageDTO = userService.queryAll(queryDTO, page);
        return new DataResponseBody(userDTOPageDTO);
    }

    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize(Permissions.USER_CREATE)
    public DataResponseBody create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO userDTO = userService.create(userCreateDTO);
        return new DataResponseBody(userDTO);
    }

    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize(Permissions.USER_EDIT)
    public DataResponseBody update(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        UserDTO userDTO = userService.update(userUpdateDTO);
        return new DataResponseBody(userDTO);
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize(Permissions.USER_DELETE)
    public DataResponseBody delete(@Valid @RequestBody UserDeleteDTO userDeleteDTO) {
        userService.delete(userDeleteDTO.getIds());
        return new DataResponseBody();
    }

    /**
     * 此接口提供给Auth模块获取用户信息使用
     * 因Auth获取用户信息在登录时是未登录状态，请不要在此添加权限校验
     */
    @ApiOperation("根据用户名称查找用户")
    @GetMapping(value = "/findUserByUsername")
    public DataResponseBody<UserContext> findUserByUsername(@RequestParam(value = "username") String username) {
        UserContext userContext = userService.findUserByUsername(username);
        return new DataResponseBody(userContext);
    }


    @ApiOperation("根据用户ID查询用户信息(服务内部访问)")
    @GetMapping(value = "/findById")
    public DataResponseBody<UserDTO> findById(@RequestParam(value = "userId") Long userId) {
        return new DataResponseBody(userService.findById(userId));
    }

    @ApiOperation("根据用户昵称搜索用户列表")
    @GetMapping(value = "/findByNickName")
    public DataResponseBody<List<UserDTO>> findByNickName(@RequestParam(value = "nickName", required = false) String nickName) {
        return new DataResponseBody(userService.findByNickName(nickName));
    }

    @ApiOperation("根据用户ID批量查询用户信息(服务内部访问)")
    @GetMapping(value = "/findByIds")
    public DataResponseBody<List<UserDTO>> getUserList(@RequestParam(value = "ids") List<Long> ids) {
        return new DataResponseBody(userService.getUserList(ids));
    }

}
