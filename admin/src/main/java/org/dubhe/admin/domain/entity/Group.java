
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.entity.BaseEntity;

/**
 * @description 用户组实体类
 * @date 2021-05-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pt_group")
public class Group extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "description")
    private String description;
}
