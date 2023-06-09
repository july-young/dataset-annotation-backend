

package org.dubhe.biz.permission.util;

import org.apache.commons.lang3.StringUtils;
import org.dubhe.biz.base.context.DataContext;
import org.dubhe.biz.base.context.UserContext;
import org.dubhe.biz.base.dto.SysRoleDTO;
import org.dubhe.biz.base.enums.BaseErrorCodeEnum;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.db.constant.PermissionConstant;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description sql语句转换的工具类

 */
public class SqlUtil {


	/**
	 * 获取资源拥有着ID
	 *
	 * @return 资源拥有者id集合
	 */
	public static Set<Long> getResourceIds(UserContext curUser) {
		if (!Objects.isNull(DataContext.get())) {
			return DataContext.get().getResourceUserIds();
		}
		Set<Long> ids = new HashSet<>();
		ids.add(curUser.getId());
		return ids;

	}


	/**
	 * 构建目标sql语句
	 *
	 * @param originSql 		原生sql
	 * @param resourceUserIds 	所属资源用户ids
	 * @return 目标sql
	 */
	public static String buildTargetSql(String originSql, Set<Long> resourceUserIds,  UserContext userContext) {
		if(Objects.isNull(userContext)){
			throw new BusinessException(BaseErrorCodeEnum.SYSTEM_USER_IS_NOT_EXISTS.getCode(),
					BaseErrorCodeEnum.SYSTEM_USER_IS_NOT_EXISTS.getMsg());

		}
		if (isAdmin(userContext.getRoles().stream().map(SysRoleDTO::getId).collect(Collectors.toList()))) {
			return originSql;
		}
		String sqlWhereBefore = StringUtils.substringBefore(originSql.toLowerCase(), "where");
		String sqlWhereAfter = StringUtils.substringAfter(originSql.toLowerCase(), "where");
		StringBuffer buffer = new StringBuffer();
		//操作的sql拼接
		String targetSql = buffer.append(sqlWhereBefore).append(" where ").append(" origin_user_id in (")
				.append(StringUtils.join(resourceUserIds, ",")).append(") and ").append(sqlWhereAfter).toString();

		return targetSql;
	}


	/**
	 * 校验是否是管理管理员	(待权限添加获取角色再修改 默认都是管理员角色访问)
	 *
	 * @return 校验标识
	 */
	public static Boolean isAdmin(List<Long> roleIds) {
		return roleIds.contains(PermissionConstant.ADMIN_ROLE_ID);
	}

}
