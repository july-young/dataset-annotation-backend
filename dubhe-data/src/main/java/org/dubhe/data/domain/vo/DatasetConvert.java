

package org.dubhe.data.domain.vo;

import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.biz.base.vo.DatasetVO;
import org.dubhe.biz.base.vo.ProgressVO;
import org.dubhe.data.domain.entity.Dataset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


/**
 * @description 数据集转换

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatasetConvert extends BaseConvert<DatasetVO, Dataset> {

    /**
     * 数据集数据转换
     *
     * @param dataset  数据集
     * @param progress 处理数量统计对象
     * @return
     */
    @Mapping(source = "progress", target = "progress")
    DatasetVO toDto(Dataset dataset, ProgressVO progress);

}
