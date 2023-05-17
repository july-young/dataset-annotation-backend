
package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description 标签组导入DTO
 * @date 2020-10-16
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LabelGroupImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签组name
     */
    @ApiModelProperty(value = "标签组name")
    @NotNull(message = "名称不能为空")
    @Size(min = 1, max = 50, message = "name长度范围1~50")
    private String name;

    /**
     * 标签组类型
     */
    @ApiModelProperty(value = "标签组类型:0:视觉,1:文本")
    @NotNull(message = "标签组类型不能为空")
    private Integer labelGroupType;

    /**
     * 备注信息
     */
    @ApiModelProperty(notes = "备注信息")
    private String remark;


}
