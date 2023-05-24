
package org.dubhe.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.User;
import org.dubhe.biz.base.dto.UserDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.cloud.authconfig.service.AdminUserService;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description Demo服务接口

 */
public interface UserService extends AdminUserService, IService<User> {


    /**
     * 根据ID获取用户信息
     *
     * @param id 用户Id
     * @return org.dubhe.domain.dto.UserDTO 用户信息返回实例
     */
    UserDTO findById(long id);

    /**
     * 新增用户
     *
     * @param resources 用户新增实体
     * @return org.dubhe.domain.dto.UserDTO 用户信息返回实例
     */
    UserDTO create(UserCreateDTO resources);

    /**
     * 修改用户
     *
     * @param resources 用户修改请求实例
     * @return org.dubhe.domain.dto.UserDTO 用户信息返回实例
     */
    UserDTO update(UserUpdateDTO resources);

    /**
     * 批量删除用户信息
     */
    void delete(Set<Long> ids);

    /**
     * 根据用户名称获取用户信息
     */
    UserDTO findByName(String userName);

    /**
     * 修改用户密码
     *
     * @param username            账号
     * @param encryptPassword     密码
     * @return void
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     *
     * @param realName 文件名
     * @param path     文件路径
     */
    void updateAvatar(String realName, String path);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 分页查询用户列表
     *
     * @param criteria 查询条件
     * @param page     分页请求实体
     * @return java.lang.Object 用户列表返回实例
     */
    PageDTO<UserDTO> queryAll(UserQueryDTO criteria, Page page);

    /**
     * 查询用户列表
     *
     * @param criteria 用户查询条件
     * @return java.util.List<org.dubhe.domain.dto.UserDTO> 用户列表返回实例
     */
    List<UserDTO> queryAll(UserQueryDTO criteria);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response 导出http响应
     * @throws IOException 导出异常
     */
    void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 修改用户个人中心信息
     *
     * @param resources 个人用户信息修改请求实例
     */
    void updateCenter(UserCenterUpdateDTO resources);

    /**
     * 查询用户ID权限
     *
     * @param id 用户ID
     * @return java.util.Set<java.lang.String> 权限列表
     */
    Set<String> queryPermissionByUserId(Long id);

    /**
     * 用户注册信息
     *
     * @param userRegisterDTO 用户注册请求实体
     * @return org.dubhe.base.DataResponseBody 注册返回结果集
     */
    void userRegister(UserRegisterDTO userRegisterDTO);


    /**
     * 获取code通过发送邮件
     *
     * @param userRegisterMailDTO 用户发送邮件请求实体
     * @return org.dubhe.base.DataResponseBody 发送邮件返回结果集
     */
    void getCodeBySentEmail(UserRegisterMailDTO userRegisterMailDTO);


    /**
     * 邮箱修改
     *
     * @param userEmailUpdateDTO 修改邮箱请求实体
     * @return org.dubhe.base.DataResponseBody 修改邮箱返回结果集
     */
    void resetEmail(UserEmailUpdateDTO userEmailUpdateDTO);

    /**
     * 密码重置接口
     *
     * @param userResetPasswordDTO 密码修改请求参数
     * @return org.dubhe.base.DataResponseBody 密码修改结果集
     */
    void resetPassword(UserResetPasswordDTO userResetPasswordDTO);

    /**
     * 登录
     *
     * @param authUserLoginDTO 登录请求实体
     */
    AuthUserLoginResultDTO login(AuthUserLoginDTO authUserLoginDTO);

    /**
     * 根据用户昵称获取用户信息
     */
    List<UserDTO> findByNickName(String nickName);

    /**
     * 根据用户id批量查询用户信息
     */
    List<UserDTO> getUserList(List<Long> ids);


}
