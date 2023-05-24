

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.db.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 用户查询DTO

 */

@Data
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = -6863842533017963450L;
    @Query
    private Long id;

    @Query(blurry = "email,username,nick_name")
    private String blurry;

    @Query
    private Boolean enabled;

    @Query(propName = "deleted", type = Query.Type.EQ)
    private Boolean deleted = false;

    private Long roleId;


    @Query(propName = "create_time", type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
