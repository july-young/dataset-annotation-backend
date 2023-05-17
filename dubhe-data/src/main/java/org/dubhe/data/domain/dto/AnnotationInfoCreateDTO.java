

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 标注文件保存
 * @date 2020-04-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnotationInfoCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标注信息")
    @NotBlank(message = "标注信息不能为空")
    private String annotation;

    @ApiModelProperty(value = "文件id")
    @NotNull(message = "文件id不能为空")
    private Long id;

    @ApiModelProperty(value = "数据集ID")
    private Long datasetId;

    @ApiModelProperty(value = "当前版本")
    private String currentVersionName;

    @ApiModelProperty(value = "数据集类型")
    private Integer dataType;

}
