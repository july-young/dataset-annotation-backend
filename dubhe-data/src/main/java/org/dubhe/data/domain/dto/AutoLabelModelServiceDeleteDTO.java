

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AutoLabelModelServiceDeleteDTO implements Serializable {


    @ApiModelProperty(value = "id列表")
    @NotEmpty(message = "服务ids不能为空")
    private List<Long> ids;


}
