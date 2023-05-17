
package org.dubhe.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dubhe.admin.domain.dto.AuthCodeCreateDTO;
import org.dubhe.admin.domain.dto.AuthCodeQueryDTO;
import org.dubhe.admin.domain.dto.AuthCodeUpdateDTO;
import org.dubhe.admin.domain.dto.RoleAuthUpdateDTO;
import org.dubhe.admin.domain.entity.Auth;
import org.dubhe.admin.domain.vo.AuthVO;
import org.dubhe.biz.db.utils.PageDTO;

import java.util.List;
import java.util.Set;

/**
 * @description 权限组服务类
 */
public interface AuthGroupService extends IService<Auth> {

    /**
     * 分页查询权限组信息
     */
    PageDTO<AuthVO> page(AuthCodeQueryDTO authCodeQueryDTO);

    /**
     * 创建权限组
     */
    void create(AuthCodeCreateDTO authCodeCreateDTO);

    /**
     * 修改权限组信息
     */
    void update(AuthCodeUpdateDTO authCodeUpdateDTO);

    /**
     * 批量删除权限组
     */
    void delete(Set<Long> ids);

    /**
     * 修改角色-权限组绑定关系
     */
    void updateRoleAuth(RoleAuthUpdateDTO roleAuthUpdateDTO);

    /**
     * 获取权限组列表
     */
    List<AuthVO> getAuthCodeList();

}
