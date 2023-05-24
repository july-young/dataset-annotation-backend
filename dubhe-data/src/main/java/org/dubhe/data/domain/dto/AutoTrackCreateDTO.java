

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @descritption 自动跟踪

 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AutoTrackCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "code:199算法失败，200成功")
    @NotNull(message = "code不能为空")
    private Integer code;

    @ApiModelProperty(notes = "调用结果信息: success,  ret")
    private String msg;

    @ApiModelProperty(notes = "回调数据")
    private String data;

    @ApiModelProperty(notes = "追踪编号")
    private String traceId;

}
