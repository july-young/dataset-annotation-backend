
package org.dubhe.recycle.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @description 回收任务执行列表
 * @date 2021-01-27
 */
@Data
public class RecycleTaskDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "待回收任务ID")
    @NotEmpty(message = "请选择回收任务")
    private List<Long> recycleTaskIdList;

}
