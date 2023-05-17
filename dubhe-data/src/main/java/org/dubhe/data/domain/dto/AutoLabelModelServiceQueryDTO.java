

package org.dubhe.data.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.base.PageQueryBase;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class AutoLabelModelServiceQueryDTO extends PageQueryBase{

    private Integer modelType;

    private Integer status;

    private String searchContent;

}
