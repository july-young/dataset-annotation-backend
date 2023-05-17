

package org.dubhe.data.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AutoLabelModelServicePodVO implements Serializable {

    @ApiModelProperty("容器名")
    private String podName;

    @ApiModelProperty("资源名")
    private String namespace;

}
