package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dubhe.biz.db.entity.BaseEntity;
import org.dubhe.data.constant.ConversionStatusEnum;
import org.dubhe.data.domain.dto.DatasetVersionCreateDTO;

import java.sql.Timestamp;

/**
 * @description 数据集版本管理
 * @date 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("data_dataset_version")
@ApiModel(value = "Dataset版本对象", description = "数据集版本管理")
@Builder
@AllArgsConstructor
public class DatasetVersion extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "所属数据集ID")
    private Long datasetId;

    @ApiModelProperty(value = "版本号")
    private String versionName;

    @ApiModelProperty(value = "版本说明")
    private String versionNote;

    @ApiModelProperty(value = "来源版本号")
    private String versionSource;

    @ApiModelProperty(value = "版本信息存储url")
    private String versionUrl;

    @ApiModelProperty(value = "版本信息转换")
    private Integer dataConversion;

    @TableField(value = "deleted",fill = FieldFill.INSERT)
    private Boolean deleted = false;

    @ApiModelProperty(value = "资源拥有人id")
    private Long originUserId;

    @ApiModelProperty(value = "是否生成ofRecord文件")
    private Boolean ofRecord;

    @ApiModelProperty(value = "格式")
    private String format;

    public DatasetVersion() {
    }

    public DatasetVersion(String versionSource, String versionUrl, DatasetVersionCreateDTO datasetVersionCreateDTO) {
        this.datasetId = datasetVersionCreateDTO.getDatasetId();
        this.versionName = datasetVersionCreateDTO.getVersionName();
        this.versionNote = datasetVersionCreateDTO.getVersionNote();
        this.versionSource = versionSource;
        this.versionUrl = versionUrl;
    }

    public DatasetVersion(Long datasetId, String versionName, String versionNote) {
        this.datasetId = datasetId;
        this.versionName = versionName;
        this.versionUrl = "dataset/"+datasetId +"/versionFile/"+versionName;
        this.dataConversion = ConversionStatusEnum.HAS_CONVERTED.getValue();
        this.versionNote = versionNote;
    }

}
