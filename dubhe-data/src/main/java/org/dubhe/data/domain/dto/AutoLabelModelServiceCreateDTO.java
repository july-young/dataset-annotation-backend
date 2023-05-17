

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AutoLabelModelServiceCreateDTO implements Serializable {

    @ApiModelProperty("模型服务名称")
    @NotNull(message = "模型服务名称不能为空")
    @Size(min = 1, max = 32, message = "模型服务名长度范围只能是1~32", groups = Create.class)
    private String name;

    @ApiModelProperty(value = "模型类别")
    @NotNull(message = "模型类别不能为空")
    private Integer modelType;

    @ApiModelProperty(value = "服务描述")
    private String desc;

    @ApiModelProperty(value = "模型版本ID")
    private Long modelBranchId;

    @ApiModelProperty(value = "模型版本上级ID")
    private Long modelParentId;

    @ApiModelProperty(value = "镜像ID")
    @NotNull(message = "镜像ID不能为空")
    private Long imageId;

    @ApiModelProperty(value = "镜像名称")
    @NotNull(message = "镜像名称不能为空")
    private String imageName;

    @ApiModelProperty(value = "算法ID")
    @NotNull(message = "算法ID不能为空")
    private Long algorithmId;

    @ApiModelProperty(value = "节点类型 0-cpu 1-gpu")
    @NotNull(message = "节点类型不能为空")
    private Integer resourcesPoolType;

    @ApiModelProperty(value = "节点规格")
    @NotNull(message = "节点规格不能为空")
    private String resourcesPoolSpecs;

    @ApiModelProperty(value = "服务数量")
    @NotNull(message = "服务数量数量不能为空")
    @Size(min = 1, message = "模型服务数据不能小于1", groups = Create.class)
    private Integer instanceNum;


    public @interface Create {
    }

}
