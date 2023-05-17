package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.dubhe.admin.domain.entity.AuthPermission;

import java.util.Collection;

public interface AuthPermissionMapper extends BaseMapper<AuthPermission> {
    Integer insertBatchSomeColumn(Collection<AuthPermission> collection);
}
