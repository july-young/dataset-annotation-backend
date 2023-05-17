
package org.dubhe.data.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description ofRecord任务详情
 * @date 2020-09-04
 */
@Data
public class OfRecordTaskDto {

    private Long id;
    private String datasetPath;
    private Map<String, String> datasetLabels;
    private List<String> files;
    private Integer partNum;
    private Long datasetVersionId;
    private String reTaskId;
    private int algorithm;
}
