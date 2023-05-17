
package org.dubhe.biz.base.service;

import org.dubhe.biz.base.context.UserContext;

/**
 * @description 获取用户上下文接口
 * @date 2020-12-07
 */
public interface UserContextService {

    /**
     * @return 用户上下文信息
     */
    UserContext getCurUser();

    /**
     * @return 用户ID
     */
    Long getCurUserId();

}
