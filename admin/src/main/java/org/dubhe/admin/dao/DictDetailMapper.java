
package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dubhe.admin.domain.entity.DictDetail;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @description  字典详情 mapper

 */
public interface DictDetailMapper extends BaseMapper<DictDetail> {

    Integer insertBatchSomeColumn(Collection<DictDetail> collection);

    @Select("select * from dict_detail where dict_id=#{dictId}")
    List<DictDetail> selectByDictId(Long dictId);
}
