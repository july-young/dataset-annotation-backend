package org.dubhe.admin.dao.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Set;

/**
 * @description 菜单sql构建类
 * @date 2020-04-02
 */
public class MenuProvider {
    public String findByRolesIdInAndTypeNotOrderBySortAsc(Map<String, Object> para) {
        Set<Long> roleIds = (Set) para.get("roleIds");
        int type = (int) para.get("type");
        StringBuffer roleIdsql = new StringBuffer("rm.role_id in (-1 ");
        roleIds.forEach(id -> {
            roleIdsql.append("," + id.toString());
        });
        roleIdsql.append(" )");
        return new SQL() {{
            SELECT_DISTINCT("m.*");
            FROM("menu m, roles_menus rm ");
            WHERE(roleIdsql + " and m.id=rm.menu_id and m.deleted = 0 and m.type<>" + type);
            ORDER_BY("pid, sort, id");
        }}.toString();
    }
}
