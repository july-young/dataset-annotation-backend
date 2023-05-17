
package org.dubhe.admin.service.convert;

import org.dubhe.admin.domain.dto.DictDTO;
import org.dubhe.admin.domain.entity.Dict;
import org.dubhe.biz.db.base.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @description 字典 转换类
 * @date 2020-06-01
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictConvert extends BaseConvert<DictDTO, Dict> {

}
