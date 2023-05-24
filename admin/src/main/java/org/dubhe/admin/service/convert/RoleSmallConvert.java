package org.dubhe.admin.service.convert;

import org.dubhe.admin.domain.dto.RoleSmallDTO;
import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.admin.domain.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @description 角色 转换类

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallConvert extends BaseConvert<RoleSmallDTO, Role> {

}
