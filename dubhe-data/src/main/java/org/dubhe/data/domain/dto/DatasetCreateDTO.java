

package org.dubhe.data.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.base.annotation.EnumValue;
import org.dubhe.biz.base.enums.DatasetTypeEnum;
import org.dubhe.cloud.authconfig.utils.JwtUtils;
import org.dubhe.data.constant.AnnotateTypeEnum;
import org.dubhe.data.constant.Constant;
import org.dubhe.data.constant.DatatypeEnum;
import org.dubhe.data.domain.entity.Dataset;
import org.dubhe.data.machine.constant.DataStateCodeConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description 数据集

 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DatasetCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "数据名不能为空", groups = Create.class)
    @Size(min = 1, max = 50, message = "数据名长度范围只能是1~50", groups = Create.class)
    private String name;

    @ApiModelProperty(notes = "备注信息")
    private String remark;

    @ApiModelProperty(notes = "类型 0: private 私有数据,  1:team  团队数据  2:public 公开数据")
    @NotNull(message = "类型不能为空", groups = Create.class)
    @EnumValue(enumClass = DatasetTypeEnum.class, enumMethod = "isValid",
            message = "类型参数不对,请使用 0-私有数据,  1-团队数据  2-公开数据", groups = Create.class)
    private Integer type;

    @ApiModelProperty(notes = "数据类型:0图片，1视频, 2文本")
    @NotNull(message = "数据类型不能为空", groups = Create.class)
    @EnumValue(enumClass = DatatypeEnum.class, enumMethod = "isValid", message = Constant.DATA_TYPE_RULE, groups = Create.class)
    private Integer dataType;

    @ApiModelProperty(notes = "标注类型：101分类,102目标检测,201目标跟踪 301文本分类")
    @NotNull(message = "数据用于标注的类型不能为空", groups = Create.class)
    @EnumValue(enumClass = AnnotateTypeEnum.class, enumMethod = "isValid", message = Constant.ANNOTATE_TYPE_RULE, groups = Create.class)
    private Integer annotateType;

    @ApiModelProperty(notes = "标签组Id")
    private Long labelGroupId;

    @ApiModelProperty(value = "预置标签类型 2:imageNet  3:MS COCO")
    private Integer presetLabelType;

    @ApiModelProperty(value = "是否用户导入")
    @TableField(value = "is_import")
    private boolean isImport;

    @ApiModelProperty(value = "模板")
    private Integer templateType;

    @ApiModelProperty(value = "所属模块")
    private Integer module;

    public @interface Create {
    }

    /**
     * DatasetCreateDTO 转换Dataset
     */
    public static Dataset from(DatasetCreateDTO datasetCreateDTO) {
        Dataset dataset = new Dataset(datasetCreateDTO);
        dataset.setStatus(DataStateCodeConstant.NOT_ANNOTATION_STATE);
        dataset.setModule(datasetCreateDTO.getModule());
        return dataset;
    }

    /**
     * 更新数据集
     */
    public static Dataset update(DatasetCreateDTO datasetCreateDTO) {
        return new Dataset(datasetCreateDTO);
    }

}
