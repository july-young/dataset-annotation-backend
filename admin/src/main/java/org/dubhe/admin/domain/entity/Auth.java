
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.dubhe.biz.db.entity.BaseEntity;

/**
 * @description 权限组实体类
 * @date 2021-05-14
 */
@Data
@TableName("auth_group")
public class Auth extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "auth_code")
    private String authCode;

    @TableField(value = "description")
    private String description;

}
