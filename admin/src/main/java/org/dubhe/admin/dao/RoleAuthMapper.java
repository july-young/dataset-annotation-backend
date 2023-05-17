package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.dubhe.admin.domain.entity.RoleAuth;

import java.util.Collection;


public interface RoleAuthMapper extends BaseMapper<RoleAuth> {
    Integer insertBatchSomeColumn(Collection<RoleAuth> collection);
}
