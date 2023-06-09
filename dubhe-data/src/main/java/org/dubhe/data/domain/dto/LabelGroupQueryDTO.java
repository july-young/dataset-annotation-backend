
package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 标签组列表DTO

 */
@Data
public class LabelGroupQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签组类型
     */
    @ApiModelProperty(value = "标签组类型(0: private 私有标签组,  1:public 公开标签组)")
    @NotNull(message = "标签组类型不能为空")
    private Integer type;

    /**
     * 数据集数据类型
     */
    @ApiModelProperty(value = "数据类型:0图片，1视频，2文本")
    @NotNull(message = "数据类型不能为空")
    private Integer dataType;

    /**
     * 数据集标注类型
     */
    @ApiModelProperty(value = "标注类型：1目标检测,2分类,5目标跟踪,7语义分割")
    @NotNull(message = "标注类型不能为空")
    private Integer annotateType;
}
