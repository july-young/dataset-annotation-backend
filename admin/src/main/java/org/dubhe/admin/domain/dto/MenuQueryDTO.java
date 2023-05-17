

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.db.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description 菜单查询实体类
 * @date 2020-06-01
 */
@Data
public class MenuQueryDTO {

    @Query(blurry = "name,path,component_name")
    private String blurry;

    @Query(propName = "create_time", type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;


    @Query(propName = "deleted", type = Query.Type.EQ)
    private Boolean deleted = false;

}
