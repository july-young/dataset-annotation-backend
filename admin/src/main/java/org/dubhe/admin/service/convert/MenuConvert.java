package org.dubhe.admin.service.convert;


import org.dubhe.admin.domain.dto.MenuDTO;
import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.admin.domain.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @description 菜单 转换类

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuConvert extends BaseConvert<MenuDTO, Menu> {

}
