

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description pod的实体类
 * @date 2020-06-03
 */
@Data
public class PodDTO {

    @ApiModelProperty(value = "pod的name")
    private String podName;

    @ApiModelProperty(value = "pod的内存")
    private String podMemory;

    @ApiModelProperty(value = "pod的cpu")
    private String podCpu;

    @ApiModelProperty(value = "pod的显卡")
    private String podCard;

    @ApiModelProperty(value = "pod的状态")
    private String status;

    @ApiModelProperty(value = "node的name")
    private String nodeName;

    @ApiModelProperty(value = "pod的创建时间")
    private String podCreateTime;


}
