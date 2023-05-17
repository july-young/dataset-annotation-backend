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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dubhe.admin.client.AuthServiceClient;
import org.dubhe.admin.dao.*;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.Role;
import org.dubhe.admin.domain.entity.User;
import org.dubhe.admin.domain.entity.UserAvatar;
import org.dubhe.admin.domain.entity.UserConfig;
import org.dubhe.admin.domain.entity.UserRole;
import org.dubhe.admin.domain.vo.EmailVo;
import org.dubhe.admin.enums.UserMailCodeEnum;
import org.dubhe.admin.event.EmailEventPublisher;
import org.dubhe.admin.service.UserService;
import org.dubhe.admin.service.convert.UserConvert;
import org.dubhe.biz.base.constant.AuthConst;
import org.dubhe.biz.base.constant.ResponseCode;
import org.dubhe.biz.base.constant.UserConstant;
import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.dto.*;
import org.dubhe.biz.base.enums.BaseErrorCodeEnum;
import org.dubhe.biz.base.enums.SwitchEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.exception.CaptchaException;
import org.dubhe.biz.base.utils.DateUtil;
import org.dubhe.biz.base.utils.RandomUtil;
import org.dubhe.biz.base.utils.RsaEncrypt;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.dataresponse.factory.DataResponseFactory;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.db.utils.PageUtil;
import org.dubhe.biz.db.utils.WrapperHelp;
import org.dubhe.biz.file.utils.DubheFileUtil;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.dubhe.biz.permission.annotation.DataPermissionMethod;
import org.dubhe.biz.permission.base.BaseService;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.dubhe.cloud.authconfig.dto.JwtUserDTO;
import org.dubhe.cloud.authconfig.factory.PasswordEncoderFactory;
import org.dubhe.cloud.authconfig.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description Demo服务接口实现类
 * @date 2020-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${rsa.private_key}")
    private String privateKey;

    @Value("${initial_password}")
    private String initialPassword;

    @Value("${user.config.notebook-delay-delete-time}")
    private Integer defaultNotebookDelayDeleteTime;

    @Value("${user.config.cpu-limit}")
    private Integer cpuLimit;

    @Value("${user.config.memory-limit}")
    private Integer memoryLimit;

    @Value("${user.config.gpu-limit}")
    private Integer gpuLimit;

    @Value("${vis.public_key}")
    private String visPublicKey;

    @Autowired
    private UserMapper userMapper;


    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserConvert userConvert;

    @Autowired
    private UserAvatarMapper userAvatarMapper;


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private EmailEventPublisher publisher;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 测试标识 true:允许debug false:拒绝debug
     */
    @Value("${debug.flag}")
    private Boolean debugFlag;

    private final String LOCK_SEND_CODE = "LOCK_SEND_CODE";

    /**
     * 分页查询用户列表
     */
    @Override
    public PageDTO<UserDTO> queryAll(UserQueryDTO criteria, Page page) {
        IPage<User> users;
        if (criteria.getRoleId() == null) {
            users = userMapper.selectCollPage(page, WrapperHelp.getWrapper(criteria));
        } else {
            users = userMapper.selectCollPageByRoleId(page, WrapperHelp.getWrapper(criteria), criteria.getRoleId());
        }
        return PageUtil.toPage(users, userConvert::toDto);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<UserDTO> queryAll(UserQueryDTO criteria) {
        List<User> users = userMapper.selectCollList(WrapperHelp.getWrapper(criteria));
        return userConvert.toDto(users);
    }

    /**
     * 根据ID获取用户信息
     */
    @Override
    public UserDTO findById(long id) {
        User user = userMapper.selectCollById(id);
        return userConvert.toDto(user);
    }


    /**
     * 新增用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(UserCreateDTO resources) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactory.getPasswordEncoder();
        if (!Objects.isNull(userMapper.findByUsername(resources.getUsername()))) {
            throw new BusinessException("用户名已存在");
        }
        if (userMapper.findByEmail(resources.getEmail()) != null) {
            throw new BusinessException("邮箱已存在");
        }
        resources.setPassword(passwordEncoder.encode(initialPassword));

        User user = User.builder().build();
        BeanUtils.copyProperties(resources, user);

        userMapper.insert(user);
        for (Role role : resources.getRoles()) {
            roleMapper.tiedUserRole(user.getId(), role.getId());
        }

        return userConvert.toDto(user);
    }


    /**
     * 修改用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO update(UserUpdateDTO resources) {

        //修改管理员信息校验
        BaseService.isAdmin();

        User user = userMapper.selectCollById(resources.getId());
        User userTmp = userMapper.findByUsername(resources.getUsername());
        if (userTmp != null && !user.equals(userTmp)) {
            throw new BusinessException("用户名已存在");
        }
        userTmp = userMapper.findByEmail(resources.getEmail());
        if (userTmp != null && !user.equals(userTmp)) {
            throw new BusinessException("邮箱已存在");
        }
        roleMapper.untiedUserRoleByUserId(user.getId());
        for (Role role : resources.getRoles()) {
            roleMapper.tiedUserRole(user.getId(), role.getId());
        }
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setRemark(resources.getRemark());
        user.setSex(resources.getSex());
        userMapper.updateById(user);
        return userConvert.toDto(user);
    }


    /**
     * 批量删除用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            Long adminId = UserConstant.ADMIN_USER_ID;
            if (ids.contains(adminId)) {
                throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_DELETE);
            }
            userMapper.deleteBatchIds(ids);
        }
    }


    /**
     * 根据用户名称获取用户信息
     */
    @Override
    public UserDTO findByName(String userName) {
        User user = userMapper.findByUsername(userName);
        if (user == null) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl findByName user is null");
            throw new BusinessException("user not found");
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        List<Role> roles = roleMapper.findRolesByUserId(user.getId());

        List<SysRoleDTO> sysRoleDTOList = roles.stream().map(a -> {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            sysRoleDTO.setId(a.getId());
            sysRoleDTO.setName(a.getName());
            return sysRoleDTO;
        }).collect(Collectors.toList());

        dto.setRoles(sysRoleDTOList);
        return dto;

    }

    /**
     * 修改用户个人中心信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(UserCenterUpdateDTO resources) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, resources.getId())
        );
        user.setNickName(resources.getNickName());
        user.setRemark(resources.getRemark());
        user.setPhone(resources.getPhone());
        user.setSex(resources.getSex());
        userMapper.updateById(user);
    }


    /**
     * 修改用户密码
     *
     * @param username 账号
     * @param pass     密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userMapper.updatePass(username, pass, new Date());
    }


    /**
     * 修改用户头像
     *
     * @param realName 名称
     * @param path     头像路径
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String realName, String path) {
        User user = userMapper.findByUsername(JwtUtils.getCurUser().getUsername());
        UserAvatar userAvatar = user.getUserAvatar();
        if (userAvatar == null) {
            userAvatar = new UserAvatar();
            userAvatar.setRealName(realName);
            userAvatar.setPath(path);
            userAvatarMapper.insert(userAvatar);
            user.setAvatarId(userAvatar.getId());
            userMapper.updateById(user);
        } else {
            userAvatar.setRealName(realName);
            userAvatar.setPath(path);
            userMapper.updateById(user);
        }
    }

    /**
     * 修改用户邮箱
     *
     * @param username 用户名称
     * @param email    用户邮箱
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userMapper.updateEmail(username, email);
    }


    /**
     * 用户信息导出
     */
    @Override
    public void download(List<UserDTO> userDTOList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("最后修改密码的时间", userDTO.getLastPasswordResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        DubheFileUtil.downloadExcel(list, response);
    }


    /**
     * 查询用户ID权限
     */
    @Override
    public Set<String> queryPermissionByUserId(Long id) {
        return userMapper.queryPermissionByUserId(id);
    }

    /**
     * 用户注册信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userRegister(UserRegisterDTO userRegisterDTO) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactory.getPasswordEncoder();
        //用户信息校验
        checkoutUserInfo(userRegisterDTO);
        String encode = passwordEncoder.encode(RsaEncrypt.decrypt(userRegisterDTO.getPassword(), privateKey));
        try {
            User newUser = User.builder()
                    .email(userRegisterDTO.getEmail())
                    .enabled(true)
                    .nickName(userRegisterDTO.getNickName())
                    .password(encode)
                    .phone(userRegisterDTO.getPhone())
                    .sex(userRegisterDTO.getSex() == 1 ? UserConstant.SEX_MEN : UserConstant.SEX_WOMEN)
                    .username(userRegisterDTO.getUsername()).build();

            //新增用户注册信息
            userMapper.insert(newUser);
            UserRole userRole = UserRole.builder().roleId((long) UserConstant.REGISTER_ROLE_ID).userId(newUser.getId()).build();
            //绑定用户默认权限
            userRoleMapper.insert(userRole);

        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "userServiceImpl userRegister error , param:{} error:{}", userRegisterDTO, e);
            throw new BusinessException(BaseErrorCodeEnum.ERROR_SYSTEM);
        }

    }


    /**
     * 获取code通过发送邮件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getCodeBySentEmail(UserRegisterMailDTO userRegisterMailDTO) {
        String email = userRegisterMailDTO.getEmail();

        User dbUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
        );
        //校验用户是否注册（type : 1 用户注册 2 修改邮箱 ）
        Boolean isRegisterOrUpdate = UserMailCodeEnum.REGISTER_CODE.getValue().equals(userRegisterMailDTO.getType()) ||
                UserMailCodeEnum.MAIL_UPDATE_CODE.getValue().equals(userRegisterMailDTO.getType());
        if (!Objects.isNull(dbUser) && isRegisterOrUpdate) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl dbUser already register , dbUser:{} ", dbUser);
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_ALREADY_EXISTS);
        }


        //限制邮箱发送次数
        limitSendEmail(email);

        try {
            synchronized (LOCK_SEND_CODE) {
                //产生随机的验证码
                String code = RandomUtil.randomCode();
                //异步发送邮件
                publisher.sentEmailEvent(
                        EmailDTO.builder()
                                .code(code)
                                .subject(UserMailCodeEnum.getEnumValue(userRegisterMailDTO.getType()).getDesc())
                                .type(userRegisterMailDTO.getType())
                                .receiverMailAddress(email).build());
                //redis存储邮箱验证信息
                redisUtils.hset(
                        getSendEmailCodeRedisKeyByType(userRegisterMailDTO.getType()).concat(email),
                        email,
                        EmailVo.builder().code(code).email(email).build(),
                        UserConstant.DATE_SECOND);
            }

        } catch (Exception e) {
            redisUtils.hdel(UserConstant.USER_EMAIL_REGISTER.concat(email), email);
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl getCodeBySentEmail error , param:{} error:{}", email, e);
            throw new BusinessException(BaseErrorCodeEnum.ERROR_SYSTEM);
        }
    }


    /**
     * 邮箱修改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetEmail(UserEmailUpdateDTO userEmailUpdateDTO) {
        //校验邮箱信息
        User dbUser = checkoutEmailInfoByReset(userEmailUpdateDTO);

        try {
            User user = User.builder()
                    .id(dbUser.getId())
                    .email(userEmailUpdateDTO.getEmail()).build();
            //修改邮箱信息
            userMapper.updateById(user);
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl update email error , email:{} error:{}", userEmailUpdateDTO.getEmail(), e);
            throw new BusinessException(BaseErrorCodeEnum.ERROR_SYSTEM.getCode(),
                    BaseErrorCodeEnum.ERROR_SYSTEM.getMsg());
        }
    }


    /**
     * 密码重置接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(UserResetPasswordDTO userResetPasswordDTO) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactory.getPasswordEncoder();
        //校验 邮箱地址 和 验证码
        checkoutEmailAndCode(userResetPasswordDTO.getCode(), userResetPasswordDTO.getEmail(), UserConstant.USER_EMAIL_RESET_PASSWORD);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<User>();
        userLambdaQueryWrapper.eq(User::getEmail, userResetPasswordDTO.getEmail());
        User dbUser = userMapper.selectOne(userLambdaQueryWrapper);
        if (dbUser == null) {
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_NOT_EXISTS);
        }

        //加密密码
        String encode = passwordEncoder.encode(RsaEncrypt.decrypt(userResetPasswordDTO.getPassword(), privateKey));
        try {
            User user = User.builder().id(dbUser.getId()).password(encode).build();
            userMapper.updateById(user);
        } catch (Exception e) {
            throw new BusinessException(BaseErrorCodeEnum.ERROR_SYSTEM);
        }
    }


    /**
     * 登录
     *
     * @param authUserLoginDTO 登录请求实体
     */
    @Override
    @DataPermissionMethod
    public AuthUserLoginResultDTO login(AuthUserLoginDTO authUserLoginDTO) {
        if (!debugFlag) {
            validateCode(authUserLoginDTO.getCode(), authUserLoginDTO.getUuid());
        }
        String password = null;
        try {
            RSA rsa = new RSA(privateKey, null);
            password = new String(rsa.decrypt(authUserLoginDTO.getPassword(), KeyType.PrivateKey));
        } catch (Exception e) {
            LogUtil.error(LogEnum.BIZ_SYS, "rsa 密钥解析失败, originPassword:{} , 密钥:{}，异常：{}", authUserLoginDTO.getPassword(), KeyType.PrivateKey, e);
            throw new BusinessException("请输入正确密码");
        }

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("username", authUserLoginDTO.getUsername());
        params.put("client_id", AuthConst.CLIENT_ID);
        params.put("client_secret", AuthConst.CLIENT_SECRET);
        params.put("password", password);
        params.put("scope", "all");
        DataResponseBody<Oauth2TokenDTO> restResult = authServiceClient.postAccessToken(params);
        AuthUserLoginResultDTO dto = new AuthUserLoginResultDTO();
        if (ResponseCode.SUCCESS.equals(restResult.getCode()) && restResult.getData() != null) {
            Oauth2TokenDTO userDto = restResult.getData();
            UserDTO user = findByName(authUserLoginDTO.getUsername());
            Set<String> permissions = this.queryPermissionByUserId(user.getId());
            if (CollUtil.isEmpty(permissions)) {
                throw new BusinessException(BaseErrorCodeEnum.SYSTEM_ROLE_NOT_EXISTS);
            }
            dto.setToken(userDto.getTokenHead() + userDto.getToken());
            dto.setUser(user);
            dto.setPermissions(permissions);
        }
        return dto;
    }

    /**
     * 根据用户昵称获取用户信息
     */
    @Override
    public List<UserDTO> findByNickName(String nickName) {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .like(User::getNickName, nickName == null ? StrUtil.EMPTY : nickName));

        return userConvert.toDto(users);
    }

    /**
     * 根据用户id批量查询用户信息
     *
     * @param ids 用户id集合
     * @return org.dubhe.domain.dto.UserDTO 用户信息DTO集合
     */
    @Override
    public List<UserDTO> getUserList(List<Long> ids) {
        List<User> users = userMapper.selectBatchIds(ids);
        return userConvert.toDto(users);
    }


    /**
     * 校验验证码
     *
     * @param loginCaptcha 验证码参数
     * @param uuid         验证码redis-key
     */
    private void validateCode(String loginCaptcha, String uuid) {
        // 验证码未输入
        if (loginCaptcha == null || "".equals(loginCaptcha)) {
            throw new CaptchaException("验证码错误");
        }
        String sessionCaptcha = (String) redisUtils.get(uuid);

        if (!loginCaptcha.equalsIgnoreCase(sessionCaptcha)) {
            throw new CaptchaException("验证码错误");
        }

    }

    /**
     * 修改邮箱校验邮箱信息
     *
     * @param userEmailUpdateDTO 邮箱修改参数校验实体
     */
    private User checkoutEmailInfoByReset(UserEmailUpdateDTO userEmailUpdateDTO) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactory.getPasswordEncoder();
        String email = userEmailUpdateDTO.getEmail();
        //管理员信息校验
        BaseService.isAdmin();

        //校验用户信息是否存在
        User dbUser = userMapper.selectCollById(userEmailUpdateDTO.getUserId());
        if (ObjectUtil.isNull(dbUser)) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl dbUser is null , userId:{}", userEmailUpdateDTO.getUserId());
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_IS_NOT_EXISTS);
        }
        //校验密码是否正确
        String decryptPassword = RsaEncrypt.decrypt(userEmailUpdateDTO.getPassword(), privateKey);
        if (!passwordEncoder.matches(decryptPassword, dbUser.getPassword())) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl password error , webPassword:{}, dbPassword:{} ",
                    userEmailUpdateDTO.getPassword(), dbUser.getPassword());
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_PASSWORD_ERROR);
        }

        //邮箱唯一性校验
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, userEmailUpdateDTO.getEmail()));
        if (count > 0) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl Email already exists , email:{} ", userEmailUpdateDTO.getEmail());
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_ALREADY_EXISTS);
        }

        //校验 邮箱地址 和 验证码
        checkoutEmailAndCode(userEmailUpdateDTO.getCode(), email, UserConstant.USER_EMAIL_UPDATE);

        return dbUser;
    }


    /**
     * 限制发送次数
     *
     * @param receiverMailAddress 邮箱接受者地址
     */
    private void limitSendEmail(final String receiverMailAddress) {
        double count = redisUtils.hincr(UserConstant.USER_EMAIL_LIMIT_COUNT.concat(receiverMailAddress), receiverMailAddress, 1);
        if (count > UserConstant.COUNT_SENT_EMAIL) {
            LogUtil.error(LogEnum.SYS_ERR, "Email verification code cannot exceed {} times .", UserConstant.COUNT_SENT_EMAIL);
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_CODE_CANNOT_EXCEED_TIMES);
        } else {
            // 验证码次数凌晨清除
            String concat = UserConstant.USER_EMAIL_LIMIT_COUNT.concat(receiverMailAddress);

            Long secondsNextEarlyMorning = DateUtil.getSecondTime();

            redisUtils.expire(concat, secondsNextEarlyMorning);
        }
    }


    /**
     * 用户信息校验
     */
    private void checkoutUserInfo(UserRegisterDTO userRegisterDTO) {
        //账户唯一性校验
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userRegisterDTO.getUsername()));
        if (!ObjectUtil.isNull(user)) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl username already exists , username:{} ", userRegisterDTO.getUsername());
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USERNAME_ALREADY_EXISTS);
        }
        //校验 邮箱地址 和 验证码
        checkoutEmailAndCode(userRegisterDTO.getCode(), userRegisterDTO.getEmail(), UserConstant.USER_EMAIL_REGISTER);
    }

    /**
     * 校验 邮箱地址 和 验证码
     *
     * @param code         验证码
     * @param email        邮箱
     * @param codeRedisKey redis-key
     */
    private void checkoutEmailAndCode(String code, String email, String codeRedisKey) {
        //校验验证码是否过期
        Object emailVoObj = redisUtils.hget(codeRedisKey.concat(email), email);
        if (Objects.isNull(emailVoObj)) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl emailVo already expired , email:{} ", email);
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_REGISTER_EMAIL_INFO_EXPIRED);
        }

        //校验邮箱和验证码
        EmailVo emailVo = (EmailVo) emailVoObj;
        if (!email.equals(emailVo.getEmail()) || !code.equals(emailVo.getCode())) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl email or code error , email:{} code:{}", email, code);
            throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_EMAIL_OR_CODE_ERROR);
        }
    }


    /**
     * 获取 发送邮箱code 的 redis key
     *
     * @param type 发送邮件类型
     */
    private String getSendEmailCodeRedisKeyByType(Integer type) {
        String typeKey;
        if (UserMailCodeEnum.REGISTER_CODE.getValue().equals(type)) {
            typeKey = UserConstant.USER_EMAIL_REGISTER;
        } else if (UserMailCodeEnum.MAIL_UPDATE_CODE.getValue().equals(type)) {
            typeKey = UserConstant.USER_EMAIL_UPDATE;
        } else if (UserMailCodeEnum.FORGET_PASSWORD.getValue().equals(type)) {
            typeKey = UserConstant.USER_EMAIL_RESET_PASSWORD;
        } else {
            typeKey = UserConstant.USER_EMAIL_OTHER;
        }
        return typeKey;
    }


    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    @Override
    public UserContext findUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (Objects.isNull(user)) {
            LogUtil.error(LogEnum.SYS_ERR, "UserServiceImpl findUserByUsername user is null {}");
            throw new BusinessException("用户信息不存在!");
        }
        UserContext dto = new UserContext();
        BeanUtils.copyProperties(user, dto);
        if (user.getUserAvatar() != null && user.getUserAvatar().getPath() != null) {
            dto.setUserAvatarPath(user.getUserAvatar().getPath());
        }
        List<Role> roles = roleMapper.selectRoleByUserId(user.getId());
        if (!CollectionUtils.isEmpty(roles)) {

            List<Long> roleIds = roles.stream().map(a -> a.getId()).collect(Collectors.toList());
            //获取菜单权限
            List<SysPermissionDTO> permissions = menuMapper.selectPermissionByRoleIds(roleIds);
            //获取操作权限
            List<SysPermissionDTO> authList = permissionMapper.selectPermissinByRoleIds(roleIds);
            permissions.addAll(authList);
            Map<Long, List<SysPermissionDTO>> permissionMap = new HashMap<>(permissions.size());
            if (!CollectionUtils.isEmpty(permissions)) {
                permissionMap = permissions.stream().collect(Collectors.groupingBy(SysPermissionDTO::getRoleId));
            }

            Map<Long, List<SysPermissionDTO>> finalPermissionMap = permissionMap;
            List<SysRoleDTO> roleDTOS = roles.stream().map(a -> {
                SysRoleDTO sysRoleDTO = new SysRoleDTO();
                BeanUtils.copyProperties(a, sysRoleDTO);
                List<SysPermissionDTO> sysPermissionDTOS = finalPermissionMap.get(a.getId());
                sysRoleDTO.setPermissions(sysPermissionDTOS);
                return sysRoleDTO;
            }).collect(Collectors.toList());
            dto.setRoles(roleDTOS);
        }
        return dto;
    }
}
