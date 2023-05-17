
package org.dubhe.data.domain.dto;

import lombok.Data;
import org.dubhe.data.domain.entity.Label;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 标签修改DTO
 * @date 2020-10-15
 */
@Data
public class LabelUpdateDTO implements Serializable {

    /**
     * 标签ID
     */
    @NotNull(message = "标签id不能为空")
    private Long labelId;

    /**
     * 标签名称
     */
    @NotNull(message = "名称不能为空")
    private String name;

    /**
     * 标签颜色
     */
    @NotNull(message = "颜色不能为空")
    private String color;


    /**
     * 数据集ID
     */
    private Long datasetId;


    /**
     * 更新标签
     *
     * @param labelUpdateDTO 修改标签条件
     * @return Label        标签实体
     */
    public static Label update(LabelUpdateDTO labelUpdateDTO) {
        return new Label(labelUpdateDTO);
    }
}
