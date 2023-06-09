package org.dubhe.admin.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.dubhe.admin.domain.entity.Dict;

import java.io.Serializable;
import java.util.List;

/**
 * @description 字典 mapper

 */
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 查询实体及关联对象
     *
     * @param queryWrapper 字典wrapper对象
     * @return 字典列表
     */
    @Select("select * from dict ${ew.customSqlSegment}")
    @Results(id = "dictMapperResults",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "dictDetails",
                            column = "id",
                            many = @Many(select = "org.dubhe.admin.dao.DictDetailMapper.selectByDictId",
                                    fetchType = FetchType.LAZY))})
    List<Dict> selectCollList(@Param("ew") Wrapper<Dict> queryWrapper);

    /**
     * 分页查询实体及关联对象
     *
     * @param page 分页对象
     * @param queryWrapper 字典wrapper对象
     * @return 分页字典集合
     */
    @Select("select * from dict ${ew.customSqlSegment}")
    @ResultMap(value = "dictMapperResults")
    IPage<Dict> selectCollPage(Page<Dict> page, @Param("ew") Wrapper<Dict> queryWrapper);

    /**
     * 根据ID查询实体及关联对象
     *
     * @param id 序列id
     * @return 字典对象
     */
    @Select("select * from dict where id=#{id}")
    @ResultMap("dictMapperResults")
    Dict selectCollById(Serializable id);

    /**
     * 根据Name查询实体及关联对象
     *
     * @param name 字典名称
     * @return 字典对象
     */
    @Select("select * from dict where name=#{name}")
    @ResultMap("dictMapperResults")
    Dict selectCollByName(String name);
}
