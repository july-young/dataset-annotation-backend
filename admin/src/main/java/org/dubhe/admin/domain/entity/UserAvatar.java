package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dubhe.biz.db.entity.BaseEntity;

import java.io.Serializable;


/**
 * @description 用户头像实体
 */
@Data
@NoArgsConstructor
@TableName("user_avatar")
public class UserAvatar extends BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "real_name")
    private String realName;

    @TableField(value = "path")
    private String path;

    @TableField(value = "size")
    private String size;
}
