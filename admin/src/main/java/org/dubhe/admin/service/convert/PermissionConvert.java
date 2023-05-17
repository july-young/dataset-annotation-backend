
package org.dubhe.admin.service.convert;

import org.dubhe.admin.domain.entity.Permission;
import org.dubhe.admin.domain.vo.PermissionVO;
import org.dubhe.biz.db.base.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @description permission转换器
 * @date 2021-05-31
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionConvert extends BaseConvert<PermissionVO, Permission> {
}
