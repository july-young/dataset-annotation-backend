

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 字典详情修改DTO

 */
@Data
public class DictDetailCreateDTO implements Serializable {

    private static final long serialVersionUID = -1936563127368448645L;

    @ApiModelProperty(value = "字典标签")
    @Length(max = 255, message = "字典标签长度不能超过255")
    private String label;

    @ApiModelProperty(value = "字典值")
    @Length(max = 255, message = "字典值长度不能超过255")
    private String value;

    @ApiModelProperty(value = "排序")
    private String sort = "999";

    private Long dictId;

    private Timestamp createTime;

    public @interface Update {
    }
}
