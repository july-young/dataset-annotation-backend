

package org.dubhe.data.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 数据集数量VO

 */
@Data
public class DatasetCountVO {

    @ApiModelProperty(value = "公共数据集数量")
    private Integer publicCount;

    @ApiModelProperty(value = "个人数据集数量")
    private Integer privateCount;

    public DatasetCountVO(Integer publicCount, Integer privateCount) {
        this.publicCount = publicCount;
        this.privateCount = privateCount;
    }

}
