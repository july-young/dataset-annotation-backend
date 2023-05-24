

package org.dubhe.data.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 批量自动标注

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchAnnotationInfoCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "标注信息不能为空")
    private List<AnnotationInfoCreateDTO> annotations;

    public Map<Long, AnnotationInfoCreateDTO> toMap() {
        Map<Long, AnnotationInfoCreateDTO> res = new HashMap<>(annotations.size());
        for (AnnotationInfoCreateDTO annotation : annotations) {
            res.put(annotation.getId(), annotation);
        }
        return res;
    }

}
