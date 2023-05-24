
package org.dubhe.biz.permission.base;

import org.dubhe.biz.base.context.DataContext;
import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.enums.BaseErrorCodeEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.service.UserContextService;
import org.dubhe.biz.base.utils.SpringContextHolder;
import org.dubhe.biz.permission.util.SqlUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description 服务层基础数据公共方法类

 */
public class BaseService {

    private BaseService(){}

    /**
     * 校验是否具有管理员权限
     */
    public static void checkAdminPermission() {
        UserContextService userContextService = SpringContextHolder.getBean(UserContextService.class);
        if(!isAdmin(userContextService.getCurUser())){
            throw new BusinessException(BaseErrorCodeEnum.DATASET_ADMIN_PERMISSION_ERROR);
        }
    }

    /**
     * 校验是否具有管理员权限
     */
    public static Boolean isAdmin() {
        UserContextService userContextService = SpringContextHolder.getBean(UserContextService.class);
        return isAdmin(userContextService.getCurUser());
    }

    /**
     * 校验是否是管理管理员
     *
     * @return 校验标识
     */
    public static Boolean isAdmin(UserContext userContext) {
        if (!CollectionUtils.isEmpty(userContext.getRoles())) {
            List<Long> roleIds = userContext.getRoles().stream().map(a -> a.getId()).collect(Collectors.toList());
            return SqlUtil.isAdmin(roleIds);
        }
        return false;
    }


    /**
     * 清除本地线程数据权限数据
     */
    public static void removeContext(){
        if( !Objects.isNull(DataContext.get())){
            DataContext.remove();
        }
    }

}
