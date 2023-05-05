package org.dubhe.data.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.dubhe.data.domain.entity.AutoLabelModelService;

public interface AutoLabelModelServiceMapper extends BaseMapper<AutoLabelModelService> {

    @Update("update auto_label_model_service set status = #{status} where id = #{modelServiceId}")
    void updateStatus(@Param("modelServiceId") Long modelServiceId, @Param("status") Integer status);
}
