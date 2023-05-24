

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dubhe.biz.base.constant.NumberConstant;
import org.dubhe.biz.base.constant.SymbolConstant;
import org.dubhe.biz.db.annotation.Query;
import org.dubhe.biz.db.base.PageQueryBase;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 数据集版本查询

 */
@Data
public class DatasetVersionQueryCriteriaDTO extends PageQueryBase implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据集ID")
    @Query(propName = "dataset_id", type = Query.Type.EQ)
    @NotNull(message = "数据集ID不能为空")
    @Min(value = NumberConstant.NUMBER_0, message = "数据集ID不能小于0")
    private Long datasetId;

    @Query(propName = "deleted", type = Query.Type.EQ)
    @ApiModelProperty(hidden = true,value = SymbolConstant.ZERO)
    private int deleted;

}
