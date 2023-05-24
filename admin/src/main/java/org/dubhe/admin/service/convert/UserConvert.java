package org.dubhe.admin.service.convert;

import org.dubhe.biz.base.dto.UserDTO;
import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.admin.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @description 用户 转换类

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConvert extends BaseConvert<UserDTO, User> {
    
}
