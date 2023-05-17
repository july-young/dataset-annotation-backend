

package org.dubhe.data.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.annotation.Query;
import org.dubhe.biz.db.base.PageQueryBase;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description 数据集查询
 * @date 2020-04-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class DatasetQueryDTO extends PageQueryBase {

    @Query(type = Query.Type.IN, propName = "id")
    private Set<Long> ids;

    @Query(type = Query.Type.LIKE)
    private String name;

    private List<Long> createTime;

    @ApiModelProperty(required = false, hidden = true)
    @Query(type = Query.Type.BETWEEN, propName = "create_time")
    private List<Timestamp> createTimeSearch;

    @Query(type = Query.Type.EQ, propName = "data_type")
    private Integer dataType;

    @Query(type = Query.Type.EQ, propName = "type")
    private Integer type;

    @Query(type = Query.Type.EQ, propName = "annotate_type")
    private Integer annotateType;

    @Query(type = Query.Type.IN)
    private Set<Integer> status;

    @Query(type = Query.Type.EQ, propName = "decompress_state")
    private Integer decompressState;

    public void timeConvert() {
        if (!CollectionUtils.isEmpty(this.createTime)) {
            createTimeSearch = new ArrayList<>(createTime.size());
            createTime.forEach(aLong -> createTimeSearch.add(new Timestamp(aLong)));
        }
    }

}
