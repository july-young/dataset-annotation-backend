

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 王伟
 * @date 2022年07月12日 14:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileScreenStatSearchDTO {

    @NotNull(message = "有无标注信息选择不能为空")
    private Integer annotationResult;
    @ApiModelProperty("文件标注状态")
    private List<Integer> annotationStatus;
    @ApiModelProperty("文件标注方式")
    private List<Integer> annotationMethod;
    @ApiModelProperty("数据集标签id")
    private List<Long> labelIds;

}
