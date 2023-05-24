

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.db.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 角色请求实体DTO

 */
@Data
public class RoleQueryDTO implements Serializable {

    private static final long serialVersionUID = 1266792048261542801L;
    @Query(blurry = "name,remark")
    private String blurry;

    @Query(propName = "create_time", type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    @Query(propName = "deleted", type = Query.Type.EQ)
    private Boolean deleted = false;

}
