

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.dubhe.biz.db.entity.BaseEntity;

import java.io.Serializable;

/**
 * @description 数据集文件标注
 * @date 2021-01-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName("data_file_annotation")
@ApiModel(value = "DataFileAnnotation对象", description = "")
public class DataFileAnnotation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("数据集ID")
    private Long datasetId;

    @ApiModelProperty("标签ID")
    private Long labelId;

    @ApiModelProperty("版本文件ID")
    private Long versionFileId;

    @ApiModelProperty("预测值")
    private Double prediction;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("标注状态")
    private Integer status;

    @ApiModelProperty("版本标注")
    private Integer invariable;

    public DataFileAnnotation(Long datasetId, Long labelId, Long versionFileId, Double prediction, String fileName) {
        this.datasetId = datasetId;
        this.labelId = labelId;
        this.versionFileId = versionFileId;
        this.prediction = prediction;
        this.fileName = fileName;
    }

}
