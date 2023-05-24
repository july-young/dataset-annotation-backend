

package org.dubhe.data.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.annotation.Query;

/**
 * @description 数据集版本文件查询条件

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatasetVersionFileCriteriaVO {

    @Query(type = Query.Type.EQ, propName = "dataset_id")
    private Long datasetId;

    @Query(type = Query.Type.IN, propName = "annotation_status")
    private int annotationStatus;

}
