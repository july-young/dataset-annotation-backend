
package org.dubhe.biz.base.context;

import lombok.Data;

import java.time.Instant;

/**
 * @description 见微平台加密用户信息
 * @date 2022-07-28
 */
@Data
public class EncryptVisUser {

	private Long id;

	private String username;

	private String phone;

	private long timestamp = Instant.now().toEpochMilli();
}
