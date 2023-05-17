package org.dubhe.admin.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.service.MenuService;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.dubhe.biz.db.utils.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @description 菜单管理 控制器
 * @date 2020-06-01
 */
@Api(tags = "系统：菜单管理")
@RestController
@RequestMapping("/menus")
@SuppressWarnings("unchecked")
public class MenuController {


    @Autowired
    private MenuService menuService;

    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize(Permissions.MENU_DOWNLOAD)
    public void download(HttpServletResponse response, MenuQueryDTO queryDTO) throws IOException {
        List<MenuDTO> menuDTOList = menuService.queryAll(queryDTO);
        menuService.download(menuDTOList, response);
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/tree")
    public DataResponseBody<List<MenuTreeDTO>> getMenuTree() {
        List<MenuTreeDTO> menuTree = menuService.getMenuTree();
        return new DataResponseBody(menuTree);
    }

    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize(Permissions.MENU)
    public DataResponseBody<PageDTO<MenuDTO>> getMenus(MenuQueryDTO criteria) {
        List<MenuDTO> menuDtoList = menuService.queryAll(criteria);
        PageDTO<MenuDTO> menuDTOPageDTO = menuService.buildTree(menuDtoList);
        return new DataResponseBody(menuDTOPageDTO);
    }

    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize(Permissions.MENU_CREATE)
    public DataResponseBody create(@Valid @RequestBody MenuCreateDTO createDTO) {
        MenuDTO menuDTO = menuService.create(createDTO);
        return new DataResponseBody(menuDTO);
    }

    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize(Permissions.MENU_EDIT)
    public DataResponseBody update(@Valid @RequestBody MenuUpdateDTO updateDTO) {
        menuService.update(updateDTO);
        return new DataResponseBody();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize(Permissions.MENU_DELETE)
    public DataResponseBody delete(@Valid @RequestBody DeleteDTO deleteDTO) {
        menuService.delete(deleteDTO.getIds());
        return new DataResponseBody();
    }
}
