

package org.dubhe.data.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 数据集版本查询
 * @date 2020-6-10
 */
@Data
public class DatasetVersionQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据集ID")
    private Long id;

    @ApiModelProperty("数据集名称")
    private String name;

}
