
package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 用户配置DTO
 * @date 2021-7-1
 */
@Data
public class UserConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户 ID 不能为空")
    @ApiModelProperty("用户 ID")
    private Long userId;

    @NotNull(message = "Notebook 延迟删除时间配置不能为空")
    @ApiModelProperty("Notebook 延迟删除时间配置，单位：小时")
    private Integer notebookDelayDeleteTime;

    @NotNull(message = "CPU 资源限制配置不能为空")
    @ApiModelProperty("CPU 资源限制，单位：核")
    private Integer cpuLimit;

    @NotNull(message = "内存资源限制配置不能为空")
    @ApiModelProperty("内存资源限制，单位：Gi")
    private Integer memoryLimit;

    @NotNull(message = "GPU 资源限制配置不能为空")
    @ApiModelProperty("GPU 资源限制，单位：块")
    private Integer gpuLimit;
}
