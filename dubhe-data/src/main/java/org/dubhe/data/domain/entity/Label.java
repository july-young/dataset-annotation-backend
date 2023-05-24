

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.entity.BaseEntity;
import org.dubhe.data.domain.dto.LabelUpdateDTO;

import java.io.Serializable;

/**
 * @description 标签

 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("data_label")
@ApiModel(value = "Label对象", description = "数据集标签")
@NoArgsConstructor
@AllArgsConstructor
public class Label extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名称")
    private String name;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String color;

    private Integer type;

    public Label (LabelUpdateDTO labelUpdateDTO){
        this.name = labelUpdateDTO.getName();
        this.color = labelUpdateDTO.getColor();
    }

}
