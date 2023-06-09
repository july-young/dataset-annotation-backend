

package org.dubhe.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.dubhe.admin.domain.entity.DictDetail;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description 字典新增DTO

 */
@Data
public class DictCreateDTO implements Serializable {

    private static final long serialVersionUID = -901581636964448858L;

    @NotBlank(message = "字典名称不能为空")
    @Length(max = 255, message = "名称长度不能超过255")
    private String name;

    @Length(max = 255, message = "备注长度不能超过255")
    private String remark;

    private Timestamp createTime;

    @TableField(exist = false)
    private List<DictDetail> dictDetails;

}
