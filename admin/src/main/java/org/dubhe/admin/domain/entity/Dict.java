
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 字典实体

 */
@Data
@TableName("dict")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dict implements Serializable {

    private static final long serialVersionUID = -3995510721958462699L;
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    @NotBlank
    @Length(max = 255, message = "名称长度不能超过255")
    private String name;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Length(max = 255, message = "备注长度不能超过255")
    private String remark;


    @TableField(value = "create_time")
    private Timestamp createTime;

    @TableField(exist = false)
    private List<DictDetail> dictDetails;

    public @interface Update {
    }
}
