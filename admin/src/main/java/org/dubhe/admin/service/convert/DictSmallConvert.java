
package org.dubhe.admin.service.convert;

import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.admin.domain.dto.DictSmallQueryDTO;
import org.dubhe.admin.domain.entity.Dict;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @description 字典详情 转换类

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictSmallConvert extends BaseConvert<DictSmallQueryDTO, Dict> {

}
