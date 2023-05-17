
package org.dubhe.admin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description 用户配置 VO
 * @date 2021-7-1
 */
@Data
@Accessors(chain = true)
public class UserConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户 ID")
    private Long userId;

    @ApiModelProperty("Notebook 延迟删除时间配置，单位：小时")
    private Integer notebookDelayDeleteTime;

    @ApiModelProperty("CPU 资源限制，单位：核")
    private Integer cpuLimit;

    @ApiModelProperty("内存资源限制，单位：Gi")
    private Integer memoryLimit;

    @ApiModelProperty("GPU 资源限制，单位：块")
    private Integer gpuLimit;
}
