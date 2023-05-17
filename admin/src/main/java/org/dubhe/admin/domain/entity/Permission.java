
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dubhe.biz.db.entity.BaseEntity;

/**
 * @description
 * @date 2021-04-26
 */
@Data
@TableName("permission")
@Accessors(chain = true)
public class Permission extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableId(value = "pid")
    private Long pid;

    @TableField(value = "name")
    private String name;

    @TableField(value = "permission")
    private String permission;

}
