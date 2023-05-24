

package org.dubhe.admin.domain.dto;

import lombok.Data;
import org.dubhe.biz.db.annotation.Query;

import java.io.Serializable;

/**
 * @description  公共查询类

 */
@Data
public class DictQueryDTO implements Serializable {

    private static final long serialVersionUID = -8221913871799888949L;
    @Query(blurry = "name,remark")
    private String blurry;
}
