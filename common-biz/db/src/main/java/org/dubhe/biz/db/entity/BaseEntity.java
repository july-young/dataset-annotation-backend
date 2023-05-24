

package org.dubhe.biz.db.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * @description  Entity基础类

 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 删除标识
     **/
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted = false;

    @TableField(value = "create_user_id",fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "update_user_id",fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Timestamp createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }

    public @interface Update {
    }
}
