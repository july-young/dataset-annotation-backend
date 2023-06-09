
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dubhe.biz.base.constant.MagicNumConstant;
import org.dubhe.biz.db.base.PageQueryBase;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @description 查询资源规格
 * @date 2021-05-27
 */
@Data
@Accessors(chain = true)
public class ResourceSpecsQueryDTO extends PageQueryBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "多GPU，true：GPU数大于1核，false:GPU数等于1核")
    private Boolean multiGpu;

    @ApiModelProperty("规格名称")
    @Length(max = MagicNumConstant.THIRTY_TWO, message = "规格名称错误")
    private String specsName;

    @ApiModelProperty("规格类型(0为CPU, 1为GPU)")
    private Boolean resourcesPoolType;

    @ApiModelProperty("所属业务场景(0:通用，1：dubhe-notebook，2：dubhe-train，3：dubhe-serving,4：dubhe-tadl,5：dubhe-point-cloud,6：data-annotation,7：atlas)")
    @Min(value = MagicNumConstant.ZERO, message = "所属业务场景错误")
    @Max(value = MagicNumConstant.SEVEN, message = "所属业务场景错误")
    private Integer module;
}
