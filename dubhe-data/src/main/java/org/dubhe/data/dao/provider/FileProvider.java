

package org.dubhe.data.dao.provider;

import java.util.Collection;
import java.util.Map;

/**
 * @description File sql构建类
 * @date 2020-04-10
 */
public class FileProvider {

    /**
     * 获取数据集状态列表
     *
     * @param para       查询参数
     * @return String    sql
     */
    public String listStatistics(Map<String, Object> para) {
        Collection<Long> ids = (Collection) para.get("ids");
        StringBuffer sql = new StringBuffer(
                "select f.dataset_id, f.`status`, count(1) c from data_file f where f.dataset_id in (");

        ids.forEach(id -> sql.append(id + ","));
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") group by f.`status`, f.dataset_id ");
        return sql.toString();
    }

}
