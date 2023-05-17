

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 数据集CSV导入
 * @date 2021-03-24
 */
@Data
public class DatasetCsvImportDTO {

    @ApiModelProperty("数据集ID")
    private Long datasetId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("csv文件地址: 文件地址为minio上的bucket地址")
    private String filePath;

    @ApiModelProperty("合并列")
    private int[] mergeColumn;

    @ApiModelProperty(value = "导入表格式，是否排除头")
    private Boolean excludeHeader;

}
