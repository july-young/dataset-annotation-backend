

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
 * @description 文本文件信息
 * @date 2021-01-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TxtFile vo", description = "文本文件信息")
public class TxtFileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文本文件id")
    private Long id;

    @ApiModelProperty(value = "文本文件名称")
    private String name;

    @ApiModelProperty(value = "状态: 101-未标注, 102-手动标注中, 103-自动标注完成, 104-标注完成, 105-标注未识别")
    private Integer status;

    @ApiModelProperty(value = "数据集id")
    private Long datasetId;

    @ApiModelProperty(value = "资源访问路径")
    private String url;

    @ApiModelProperty("预测值")
    private Double prediction;

    @ApiModelProperty("标注ID")
    private List<Long> labelIdList;

    @ApiModelProperty("摘要名称")
    private String abstractName;

    @ApiModelProperty("摘要Url")
    private String abstractUrl;

    @ApiModelProperty("文本内容")
    private String content;

    @ApiModelProperty("标注信息")
    private String annotation;
}
