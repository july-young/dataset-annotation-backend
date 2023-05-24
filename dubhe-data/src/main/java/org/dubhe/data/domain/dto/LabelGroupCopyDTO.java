
package org.dubhe.data.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description 标签组复制DTO

 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LabelGroupCopyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签组 Id
     */
    @NotNull(message = "标签组Id不能为空")
    private Long id;

    /**
     * 标签组名称
     */
    @NotNull(message = "标签组名称不能为空")
    @Size(min = 1, max = 50, message = "标签组长度范围只能是1~50", groups = DatasetCreateDTO.Create.class)
    private String name;

    /**
     * 描述
     */
    private String remark;




}
