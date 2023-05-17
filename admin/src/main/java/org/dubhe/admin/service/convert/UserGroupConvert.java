package org.dubhe.admin.service.convert;

import org.dubhe.admin.domain.dto.UserGroupDTO;
import org.dubhe.admin.domain.entity.Group;
import org.dubhe.biz.db.base.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupConvert extends BaseConvert<UserGroupDTO, Group> {
    
}
