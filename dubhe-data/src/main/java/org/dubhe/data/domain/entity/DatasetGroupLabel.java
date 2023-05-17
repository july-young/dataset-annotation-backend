

package org.dubhe.data.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.entity.BaseEntity;

import java.io.Serializable;

/**
 * @description 数据集标签标签组关系表
 * @date 2020-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("data_group_label")
public class DatasetGroupLabel extends BaseEntity implements Serializable {

    /**
     * 标签Id
     */
    private Long labelId ;


    /**
     * 标签组Id
     */
    private Long labelGroupId;

}
