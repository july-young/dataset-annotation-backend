

package org.dubhe.data.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 数据集是否为导入

 */
@Data
@Builder
public class IsImportVO implements Serializable {

    /**
     * 导入状态
     */
    private Integer status;
}
