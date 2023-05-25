package org.dubhe.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dubhe.admin.dao.MenuMapper;
import org.dubhe.admin.domain.dto.*;
import org.dubhe.admin.domain.entity.Menu;
import org.dubhe.admin.domain.vo.MenuMetaVo;
import org.dubhe.admin.domain.vo.MenuVo;
import org.dubhe.admin.enums.MenuTypeEnum;
import org.dubhe.admin.service.MenuService;
import org.dubhe.admin.service.RoleService;
import org.dubhe.admin.service.convert.MenuConvert;
import org.dubhe.biz.base.exception.BusinessException;
import org.dubhe.biz.base.utils.StringUtils;
import org.dubhe.biz.db.constant.PermissionConstant;
import org.dubhe.biz.db.utils.PageDTO;
import org.dubhe.biz.db.utils.PageUtil;
import org.dubhe.biz.db.utils.WrapperHelp;
import org.dubhe.biz.file.utils.DubheFileUtil;
import org.dubhe.biz.log.enums.LogEnum;
import org.dubhe.biz.log.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @description 菜单服务 实现类

 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuConvert menuConvert;

    @Autowired
    private RoleService roleService;

    /**
     * 按条件查询菜单列表
     */
    @Override
    public List<MenuDTO> queryAll(MenuQueryDTO criteria) {
        List<Menu> menus = menuMapper.selectList(WrapperHelp.getWrapper(criteria));
        return menuConvert.toDto(menus);
    }


    /**
     * 根据id查询菜单信息
     *
     * @param id 菜单id
     * @return org.dubhe.domain.dto.MenuDTO 菜单返回实例
     */
    @Override
    public MenuDTO findById(long id) {

        Menu menu = menuMapper.selectOne(
                new LambdaQueryWrapper<Menu>().eq(Menu::getId, id).eq(Menu::getDeleted,false)
        );
        return menuConvert.toDto(menu);
    }

    /**
     * 根据角色查询菜单列表
     *
     * @param roles 角色
     * @return java.util.List<org.dubhe.domain.dto.MenuDTO> 菜单返回实例
     */
    @Override
    public List<MenuDTO> findByRoles(List<RoleSmallDTO> roles) {
        Set<Long> roleIds = roles.stream().map(RoleSmallDTO::getId).collect(Collectors.toSet());
        List<Menu> menus = menuMapper.findByRolesIdInAndTypeNotOrderBySortAsc(roleIds, 2);
        return menus.stream().map(menuConvert::toDto).collect(Collectors.toList());
    }

    /**
     * 新增菜单
     *
     * @param resources 菜单新增请求实体
     * @return org.dubhe.domain.dto.MenuDTO 菜单返回实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuDTO create(MenuCreateDTO resources) {
        if (StringUtils.isNotBlank(resources.getComponentName())) {
            if (menuMapper.findByComponentName(resources.getComponentName()) != null) {
                throw new BusinessException("路由名称已存在");
            }
        }
        if (MenuTypeEnum.LINK_TYPE.getValue().equals(resources.getType())) {
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
                throw new BusinessException("外链必须以http://或者https://开头");
            }
        }
        Menu menu = Menu.builder()
                .component(resources.getComponent())
                .cache(resources.getCache())
                .componentName(resources.getComponentName())
                .hidden(resources.getHidden())
                .layout(resources.getLayout())
                .icon(resources.getIcon())
                .name(resources.getName())
                .path(resources.getPath())
                .pid(resources.getPid())
                .permission(resources.getPermission())
                .sort(resources.getSort())
                .type(resources.getType())
                .build();
        if (MenuTypeEnum.PAGE_TYPE.getValue().equals(resources.getType())) {
            menu.setBackTo(resources.getBackTo());
            menu.setExtConfig(resources.getExtConfig());
        }
        menuMapper.insert(menu);
        //管理员新增默认权限
        roleService.tiedRoleMenu(PermissionConstant.ADMIN_ROLE_ID, menu.getId());

        return menuConvert.toDto(menu);
    }

    /**
     * 修改菜单
     *
     * @param resources 菜单修改请求实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuUpdateDTO resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BusinessException("上级不能为自己");
        }
        Menu menu = menuMapper.selectOne(
                new LambdaQueryWrapper<Menu>()
                        .eq(Menu::getId, resources.getId())
                        .eq(Menu::getDeleted, false)
        );
        if (MenuTypeEnum.LINK_TYPE.getValue().equals(resources.getType())) {
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
                throw new BusinessException("外链必须以http://或者https://开头");
            }
        }

        if (StringUtils.isNotBlank(resources.getComponentName())) {
            Menu dbMenu = menuMapper.findByComponentName(resources.getComponentName());
            if (dbMenu != null && !dbMenu.getId().equals(menu.getId())) {
                throw new BusinessException("路由名称已存在");
            }
        }
        menu.setName(resources.getName());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setType(resources.getType());
        menu.setLayout(resources.getLayout());
        menu.setPid(resources.getPid());
        menu.setSort(resources.getSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setComponentName(resources.getComponentName());
        menu.setPermission(resources.getPermission());
        if (MenuTypeEnum.PAGE_TYPE.getValue().equals(resources.getType())) {
            ExtConfigDTO extConfigDTO = analyzeBackToValue(resources.getExtConfig());
            menu.setBackTo(Objects.isNull(extConfigDTO) ? null : extConfigDTO.getBackTo());
            menu.setExtConfig(resources.getExtConfig());
        }
        menuMapper.updateById(menu);
    }


    /**
     * 解析扩展配置中 backTO 属性值
     *
     * @param extConfig 扩展配置
     * @return ExtConfigDTO扩展配置
     */
    private ExtConfigDTO analyzeBackToValue(String extConfig) {
        ExtConfigDTO dto = ExtConfigDTO.builder().build();
        try {
            if (extConfig != null) {
                dto = JSONObject.parseObject(extConfig, ExtConfigDTO.class);
            }
        } catch (Exception e) {
            LogUtil.error(LogEnum.SYS_ERR, "analyzeBackToValue error, params:{} , error:{}", extConfig, e);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        List<Menu> menus = menuMapper.selectList(new QueryWrapper<>());

        menus = menus.stream().sorted(Comparator.comparing(Menu::getPid)).collect(Collectors.toList());
        boolean next = false;
        do {
            for (int i = 0; i < menus.size(); ) {
                if (ids.contains(menus.get(i).getPid())) {
                    next = true;
                    ids.add(menus.get(i).getId());
                    menus.remove(i);
                } else {
                    i++;
                }
            }
        } while (next);

        roleService.untiedMenu(ids);
        menuMapper.deleteBatchIds(ids);
    }

    /**
     * 获取菜单树
     */
    @Override
    public List<MenuTreeDTO> getMenuTree() {

        List<Menu> menusAll = menuMapper.selectList(new QueryWrapper<>());
        menusAll = new LinkedList<>(menusAll);
        List<MenuTreeDTO> menuTreeDTOList = buildMenuTree(menusAll, 0L);
        return menuTreeDTOList;
    }

    private List<MenuTreeDTO> buildMenuTree(List<Menu> menus, Long pid) {

        List<MenuTreeDTO> menuTreeDTOList = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            if (menu.getPid().equals(pid)) {
                MenuTreeDTO dto = new MenuTreeDTO();
                dto.setId(menu.getId());
                dto.setLabel(menu.getName());
                menuTreeDTOList.add(dto);
                dto.setChildren(buildMenuTree(menus, menu.getId()));
            }
        }
        return menuTreeDTOList;
    }


    /**
     * 根据ID获取菜单列表
     */
    @Override
    public List<Menu> findByPid(long pid) {
        return menuMapper.findByPid(pid);
    }


    /**
     * 构建菜单树
     */
    @Override
    public PageDTO<MenuDTO> buildTree(List<MenuDTO> menuDTOList) {

        List<MenuDTO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDTO menuDTO : menuDTOList) {
            if (menuDTO.getPid() == 0) {
                trees.add(menuDTO);
            }
            for (MenuDTO it : menuDTOList) {
                if (it.getPid().equals(menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menuDTOList.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }

        return PageUtil.toPage(new Page(1, trees.size()), trees);
    }


    /**
     * 构建菜单树
     *
     * @param menuDTOList 菜单请求实体
     * @return java.util.List<org.dubhe.domain.vo.MenuVo> 菜单树返回实例
     */
    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDTOList) {
        List<MenuVo> list = new LinkedList<>();
        menuDTOList.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDTO> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == 0 ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if (!MenuTypeEnum.LINK_TYPE.getValue().equals(menuDTO.getType())) {
                            if (menuDTO.getPid() == 0) {
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                            } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon(), menuDTO.getLayout(), !menuDTO.getCache()));
                        if (menuDtoList != null && menuDtoList.size() != 0) {
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid() == 0) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!MenuTypeEnum.LINK_TYPE.getValue().equals(menuDTO.getType())) {
                                menuVo1.setPath(menuVo.getPath());
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                                menuVo1.setName(menuDTO.getComponentName());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }


    /**
     * 获取菜单
     */
    @Override
    public Menu findOne(Long id) {
        Menu menu = menuMapper.selectById(id);
        return menu;
    }


    /**
     * 导出菜单
     *
     * @param menuDtos 菜单列表
     * @param response
     */
    @Override
    public void download(List<MenuDTO> menuDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDTO menuDTO : menuDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单名称", menuDTO.getName());
            map.put("菜单类型", MenuTypeEnum.getEnumValue(menuDTO.getType()).getDesc());
            map.put("权限标识", menuDTO.getPermission());
            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
            map.put("创建日期", menuDTO.getCreateTime());
            list.add(map);
        }
        DubheFileUtil.downloadExcel(list, response);
    }
}
