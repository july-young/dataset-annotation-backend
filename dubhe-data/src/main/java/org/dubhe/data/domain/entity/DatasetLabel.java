

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.dubhe.biz.db.entity.BaseEntity;

import java.io.Serializable;

/**
 * @description 数据集标签
 * @date 2020-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName("data_dataset_label")
@ApiModel(value = "DatasetLabel对象", description = "")
public class DatasetLabel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据集ID")
    private Long datasetId;

    @ApiModelProperty("标签ID")
    private Long labelId;

    @ApiModelProperty("删除标识")
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    private Boolean deleted = false;

}
