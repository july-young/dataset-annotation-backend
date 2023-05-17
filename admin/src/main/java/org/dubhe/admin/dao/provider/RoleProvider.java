package org.dubhe.admin.dao.provider;

/**
 * @description  角色构建类
 * @date 2020-04-15
 */
public class RoleProvider {
    public String findRolesByUserId(Long userId) {
        StringBuffer sql = new StringBuffer("select r.* from role r, users_roles ur ");
        sql.append(" where ur.user_id=#{userId} ");
        sql.append(" and ur.role_id=r.id");
        sql.append(" and r.deleted = 0");
        return sql.toString();
    }
}
