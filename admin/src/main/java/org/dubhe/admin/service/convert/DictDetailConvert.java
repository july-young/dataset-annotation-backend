
package org.dubhe.admin.service.convert;

import org.dubhe.biz.base.dto.DictDetailDTO;
import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.admin.domain.entity.DictDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @description 字典详情 转换类

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailConvert extends BaseConvert<DictDetailDTO, DictDetail> {
}
