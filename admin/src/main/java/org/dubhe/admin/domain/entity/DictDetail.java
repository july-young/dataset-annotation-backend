
package org.dubhe.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 字典详情实体
 * @date 2020-06-01
 */
@Data
@TableName("dict_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictDetail implements Serializable {

    private static final long serialVersionUID = 299242717738411209L;
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 字典标签
     */
    @TableField(value = "label")
    @Length(max = 255, message = "字典标签长度不能超过255")
    private String label;

    /**
     * 字典值
     */
    @TableField(value = "value")
    @Length(max = 255, message = "字典值长度不能超过255")
    private String value;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private String sort = "999";

    @TableField(value = "dict_id")
    private Long dictId;

    @TableField(value = "create_time")
    private Timestamp createTime;

    public @interface Update {
    }
}
