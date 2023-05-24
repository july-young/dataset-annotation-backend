

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.db.annotation.Query;

import java.io.Serializable;

/**
 * @description 字典详情查询实体

 */
@Data
public class DictDetailQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Query(type = Query.Type.LIKE)
    private String label;

    @Query(propName = "dict_id", type = Query.Type.EQ)
    private Long dictId;
}
