

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.entity.BaseEntity;

import java.util.Objects;

/**
 * @description 文件信息

 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("data_file")
@ApiModel(value = "File对象", description = "文件信息")
public class File extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "状态:0-未标注，1-标注中，2-自动标注完成，3-已标注完成")
    private Integer status;

    @ApiModelProperty(value = "数据集id")
    private Long datasetId;

    @ApiModelProperty(value = "资源访问路径")
    private String url;

    @ApiModelProperty(value = "图片类型")
    private Integer fileType;

    @ApiModelProperty(value = "父文件id")
    private Long pid;

    @ApiModelProperty(value = "帧间隔")
    private Integer frameInterval;

    @ApiModelProperty(value = "增强类型")
    private Integer enhanceType;

    @ApiModelProperty(value = "图片宽")
    private Integer width;

    @ApiModelProperty(value = "图片高")
    private Integer height;

    @ApiModelProperty(value = "资源拥有人id")
    private Long originUserId;

    @ApiModelProperty(value = "是否上传至es")
    private Integer esTransport;

    @ApiModelProperty(value = "导入表格式，是否排除头")
    private Boolean excludeHeader;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        File file = (File) o;
        return Objects.equals(getId(), file.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
