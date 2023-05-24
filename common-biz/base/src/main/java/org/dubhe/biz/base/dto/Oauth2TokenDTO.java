
package org.dubhe.biz.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description Oauth2获取Token返回信息封装

 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2TokenDTO implements Serializable {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenHead;
    /**
     * 有效时间（秒
     */
    private int expiresIn;
}
