

package org.dubhe.data.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.annotation.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @description 文件查询条件
 * @date 2020-04-10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileQueryCriteriaVO {

    @ApiModelProperty(value = "数据集文件ID")
    @Query(type = Query.Type.IN, propName = "id")
    private Set<Long> ids;

    @ApiModelProperty(value = "数据集文件状态")
    @Query(type = Query.Type.IN)
    private List<Integer> statusList;

    @ApiModelProperty(value = "数据集文件名称")
    @Query(type = Query.Type.LIKE)
    private String name;

    @ApiModelProperty(value = "数据集文件URL")
    @Query(type = Query.Type.EQ)
    private String url;

    @ApiModelProperty(value = "数据集文件创建时间")
    @Query(type = Query.Type.BETWEEN, propName = "create_time")
    private List<Timestamp> createTime;

    @ApiModelProperty(value = "数据集ID")
    @Query(type = Query.Type.EQ, propName = "dataset_id")
    private Long datasetId;

    @ApiModelProperty(value = "排序类型")
    @Query(type = Query.Type.ORDER_BY)
    private String order;

    @ApiModelProperty(value = "数据集文件类型")
    @Query(type = Query.Type.EQ, propName = "file_type")
    private Integer fileType;

    @ApiModelProperty(value = "数据集标签ID")
    @Query(type = Query.Type.IN, propName = "label_id")
    private List<Long> labelIdList;

    @ApiModelProperty(value = "标注状态")
    private List<Integer> annotateStatusList;

    @ApiModelProperty(value = "标注方式")
    private List<Integer> annotateTypeList;

    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容")
    private String content;

    /**
     * 排序 desc | asc
     */
    @ApiModelProperty(value = "排序方式")
    private String sort;

    /**
     * pid
     */
    @ApiModelProperty(value = "视频文件id")
    @Query(type = Query.Type.EQ, propName = "pid")
    private Long pid;
}