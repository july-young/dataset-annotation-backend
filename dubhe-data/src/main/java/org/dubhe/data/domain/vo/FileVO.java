

package org.dubhe.data.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description 文件信息

 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "File vo", description = "文件信息")
public class FileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件ID")
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别, 201-目标跟踪完成")
    private Integer status;

    @ApiModelProperty(value = "数据集id")
    private Long datasetId;

    @ApiModelProperty(value = "资源访问路径")
    private String url;

    @ApiModelProperty(value = "更新用户ID")
    private Long updateUserId;

    @ApiModelProperty(value = "标注内容")
    private String annotation;

    @ApiModelProperty(value = "父文件id")
    private Long pid;

    @ApiModelProperty(value = "增强类型")
    private Integer enhanceType;

    @ApiModelProperty("预测值")
    private Double prediction;

    @ApiModelProperty("标注ID")
    private List<Long> labelIdList;

    @ApiModelProperty("摘要名称")
    private String abstractName;

    @ApiModelProperty("摘要Url")
    private String abstractUrl;

}
