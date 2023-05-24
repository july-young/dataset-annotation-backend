

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @description 删除文件

 */
@Data
public class FileDeleteDTO implements Serializable {

    @ApiModelProperty(value = "fileIds", required = true)
    @NotNull(message = "文件id不能为空")
    private List<Long> fileIds;

    @ApiModelProperty(value = "datasetIds", required = true)
    @NotNull(message = "数据集id不能为空")
    private List<Long> datasetIds;

}
