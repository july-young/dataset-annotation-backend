package org.dubhe.admin.rest;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dubhe.admin.client.AuthServiceClient;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.service.UserService;
import org.dubhe.biz.base.constant.UserConstant;
import org.dubhe.biz.base.utils.DateUtil;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.dubhe.cloud.authconfig.dto.JwtUserDTO;
import org.dubhe.cloud.authconfig.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description 系统登录 控制器

 */
@Api(tags = "系统登录")
@RestController
@RequestMapping("/auth")
@Slf4j
@SuppressWarnings("unchecked")
public class LoginController {

    @Value("${rsa.public_key}")
    private String publicKey;

    @Value("${loginCode.expiration}")
    private Long expiration;

    @Value("${loginCode.width}")
    private Integer width;

    @Value("${loginCode.height}")
    private Integer height;

    @Value("${loginCode.length}")
    private Integer length;

    @Value("${loginCode.codeKey}")
    private String codeKey;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthServiceClient authServiceClient;


    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public DataResponseBody<AuthUserLoginResultDTO> login(@Validated @RequestBody AuthUserLoginDTO authUserLoginDTO) {
        AuthUserLoginResultDTO authUserLoginResultDTO = userService.login(authUserLoginDTO);
        return new DataResponseBody(authUserLoginResultDTO);
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public DataResponseBody getCode() {
        Captcha captcha = new SpecCaptcha(width, height, length);
        String createText = captcha.text();
        String uuid = codeKey + IdUtil.simpleUUID();
        // 保存
        redisUtils.set(uuid, createText, expiration, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(4) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return new DataResponseBody(imgResult);
    }


    @ApiOperation("退出登录")
    @DeleteMapping(value = "/logout")
    public DataResponseBody logout(@RequestHeader("Authorization") String accessToken) {
        return authServiceClient.logout(accessToken);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public DataResponseBody<Map<String, Object>> info() {
        JwtUserDTO curUser = JwtUtils.getCurUser();

        Set<String> permissions = userService.queryPermissionByUserId(curUser.getCurUserId());
        Map<String, Object> authInfo = new HashMap<String, Object>(4) {{
            put("user", curUser.getUser());
            put("permissions", permissions);
        }};
        return new DataResponseBody(authInfo);
    }


    @ApiOperation("用户注册信息")
    @PostMapping(value = "/userRegister")
    public DataResponseBody userRegister(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.userRegister(userRegisterDTO);
        return new DataResponseBody();
    }


    @ApiOperation("用户忘记密码")
    @PostMapping(value = "/resetPassword")
    public DataResponseBody resetPassword(@Valid @RequestBody UserResetPasswordDTO userResetPasswordDTO) {
        userService.resetPassword(userResetPasswordDTO);
        return new DataResponseBody();
    }


    @ApiOperation("获取code通过发送邮件")
    @PostMapping(value = "/getCodeBySentEmail")
    public DataResponseBody getCodeBySentEmail(@Valid @RequestBody UserRegisterMailDTO userRegisterMailDTO) {
        userService.getCodeBySentEmail(userRegisterMailDTO);
        return new DataResponseBody();
    }


    @ApiOperation("获取公钥")
    @GetMapping(value = "/getPublicKey")
    public DataResponseBody getPublicKey() {
        return new DataResponseBody(publicKey);
    }

    /**
     * 限制登录失败次数
     *
     * @param username
     */
    private boolean limitLoginCount(final String username) {
        String concat = UserConstant.USER_LOGIN_LIMIT_COUNT.concat(username);
        double count = redisUtils.hincr(UserConstant.USER_LOGIN_LIMIT_COUNT.concat(username), concat, 1);
        if (count > UserConstant.COUNT_LOGIN_FAIL) {
            return false;
        } else {
            // 验证码次数凌晨清除
            long afterSixHourTime = DateUtil.getAfterSixHourTime();
            redisUtils.hset(concat, concat, afterSixHourTime);
        }
        return true;
    }

}
