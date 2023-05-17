

package org.dubhe.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.dubhe.admin.domain.entity.Dict;
import org.dubhe.admin.domain.entity.DictDetail;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 字典新增DTO
 * @date 2020-06-01
 */
@Data
public class DictUpdateDTO implements Serializable {

    private static final long serialVersionUID = -901581636964448858L;

    @NotNull(groups = Dict.Update.class)
    private Long id;

    @NotBlank
    @Length(max = 255, message = "名称长度不能超过255")
    private String name;

    @Length(max = 255, message = "备注长度不能超过255")
    private String remark;

    private Timestamp createTime;

    @TableField(exist = false)
    private List<DictDetail> dictDetails;

    public @interface Update {
    }
}
