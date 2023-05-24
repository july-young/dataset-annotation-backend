

package org.dubhe.biz.db.constant;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @description 权限常量

 */
@Component
@Data
public class PermissionConstant {

    /**
     * 超级用户ID
     */
    public static final long ADMIN_USER_ID = 1L;

    /**
     * 超级用户角色ID
     */
    public static final long ADMIN_ROLE_ID = 1L;

}
