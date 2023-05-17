

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.dubhe.biz.db.entity.BaseEntity;
/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auto_label_model_service")
@ApiModel(value = "模型标注服务", description = "模型标注服务")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoLabelModelService extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "模型类别")
    private Integer modelType;

    @ApiModelProperty(value = "服务描述")
    private String remark;

    @ApiModelProperty(value = "模型版本ID")
    private Long modelBranchId;

    @ApiModelProperty(value = "模型版本上级ID")
    private Long modelParentId;

    @ApiModelProperty(value = "镜像ID")
    private Long imageId;

    @ApiModelProperty(value = "镜像名称")
    private String imageName;

    @ApiModelProperty(value = "算法ID")
    private Long algorithmId;

    @ApiModelProperty(value = "状态 101-启动中 102-运行中 103-启动失败 104-停止中 105-已停止")
    private Integer status;

    @ApiModelProperty(value = "节点类型 0-cpu 1-gpu")
    private Integer resourcesPoolType;

    @ApiModelProperty(value = "节点规格")
    private String resourcesPoolSpecs;

    @ApiModelProperty(value = "服务数量")
    private Integer instanceNum;

}
