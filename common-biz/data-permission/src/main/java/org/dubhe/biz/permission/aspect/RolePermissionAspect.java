
package org.dubhe.biz.permission.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.dubhe.biz.base.enums.BaseErrorCodeEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.permission.base.BaseService;
import org.springframework.stereotype.Component;

/**
 * @description 角色权限切面(验证是否具有管理员权限)
 * @date 2020-11-26
 */
@Aspect
@Component
public class RolePermissionAspect {


    /**
     * 基于注解的切面方法
     */
    @Pointcut("@annotation(org.dubhe.biz.permission.annotation.RolePermission)")
    public void cutMethod() {

    }
    /**
     * 前置通知 验证是否具有管理员权限
     *
     * @param point 切入参数对象
     * @return 返回方法结果集
     */
    @Before(value = "cutMethod()")
    public void before(JoinPoint point) {
        if (!BaseService.isAdmin()) {
            throw new BusinessException(BaseErrorCodeEnum.DATASET_ADMIN_PERMISSION_ERROR);
        }
    }

}
